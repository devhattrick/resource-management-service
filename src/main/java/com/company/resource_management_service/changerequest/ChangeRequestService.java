package com.company.resource_management_service.changerequest;

import com.company.resource_management_service.audit.AuditService;
import com.company.resource_management_service.changerequest.apply.ChangeItemApplierFactory;
import com.company.resource_management_service.changerequest.dto.ChangeRequestItemRequest;
import com.company.resource_management_service.changerequest.dto.ChangeRequestResponse;
import com.company.resource_management_service.changerequest.dto.ChangeRequestSummaryResponse;
import com.company.resource_management_service.changerequest.dto.CreateChangeRequestRequest;
import com.company.resource_management_service.common.exception.BusinessException;
import com.company.resource_management_service.common.exception.ErrorCode;
import com.company.resource_management_service.common.exception.ResourceNotFoundException;
import com.company.resource_management_service.common.web.PageResponse;
import com.company.resource_management_service.developer.DeveloperProfile;
import com.company.resource_management_service.developer.DeveloperProfileRepository;
import com.company.resource_management_service.security.SecurityUtils;
import com.company.resource_management_service.user.entity.AppUser;
import com.company.resource_management_service.user.repository.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class ChangeRequestService {

    private static final String PERM_APPROVE = "SKILL_APPROVE";

    private final ChangeRequestRepository changeRequestRepository;
    private final DeveloperProfileRepository profileRepository;
    private final AppUserRepository userRepository;
    private final ChangeItemApplierFactory applierFactory;
    private final AuditService auditService;
    private final ChangeRequestMapper mapper;

    public ChangeRequestService(ChangeRequestRepository changeRequestRepository,
                                DeveloperProfileRepository profileRepository,
                                AppUserRepository userRepository,
                                ChangeItemApplierFactory applierFactory,
                                AuditService auditService,
                                ChangeRequestMapper mapper) {
        this.changeRequestRepository = changeRequestRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.applierFactory = applierFactory;
        this.auditService = auditService;
        this.mapper = mapper;
    }

    @Transactional
    public ChangeRequestResponse create(CreateChangeRequestRequest request) {
        UUID userId = SecurityUtils.currentUserId();
        DeveloperProfile developer = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION,
                        "Current user has no developer profile"));

        ChangeRequest cr = new ChangeRequest();
        cr.setDeveloper(developer);
        cr.setRequestedBy(userRepository.getReferenceById(userId));
        cr.setStatus(ChangeRequestStatus.DRAFT);
        cr.setNote(request.note());

        for (ChangeRequestItemRequest ir : request.items()) {
            ChangeRequestItem item = new ChangeRequestItem();
            item.setTargetType(ChangeTargetType.SKILL);
            item.setAction(ir.action());
            item.setTargetRefId(ir.targetRefId());
            SkillChangePayload payload = new SkillChangePayload();
            payload.setProficiencyLevel(ir.proficiencyLevel());
            payload.setMainStack(ir.mainStack());
            payload.setYearsOfExperience(ir.yearsOfExperience());
            payload.setLastUsedDate(ir.lastUsedDate());
            item.setPayload(payload);
            cr.addItem(item);
        }

        ChangeRequest saved = changeRequestRepository.save(cr);
        return mapper.toResponse(reload(saved.getId()));
    }

    @Transactional
    public ChangeRequestResponse submit(UUID id) {
        ChangeRequest cr = reload(id);
        assertRequester(cr);
        requireStatus(cr, ChangeRequestStatus.DRAFT);
        if (cr.getItems().isEmpty()) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, "Cannot submit a request with no items");
        }
        cr.setStatus(ChangeRequestStatus.SUBMITTED);
        cr.setSubmittedAt(OffsetDateTime.now());
        auditService.record("CR_SUBMITTED", "ChangeRequest", cr.getId(), SecurityUtils.currentUserId(), null, null);
        return mapper.toResponse(cr);
    }

    @Transactional
    public ChangeRequestResponse cancel(UUID id) {
        ChangeRequest cr = reload(id);
        assertRequester(cr);
        if (cr.getStatus() != ChangeRequestStatus.DRAFT && cr.getStatus() != ChangeRequestStatus.SUBMITTED) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION,
                    "Only DRAFT or SUBMITTED requests can be cancelled");
        }
        cr.setStatus(ChangeRequestStatus.CANCELLED);
        auditService.record("CR_CANCELLED", "ChangeRequest", cr.getId(), SecurityUtils.currentUserId(), null, null);
        return mapper.toResponse(cr);
    }

    @Transactional
    public ChangeRequestResponse approve(UUID id, String comment) {
        ChangeRequest cr = reload(id);
        requireStatus(cr, ChangeRequestStatus.SUBMITTED);

        UUID approverId = SecurityUtils.currentUserId();
        AppUser approver = userRepository.getReferenceById(approverId);
        DeveloperProfile developer = cr.getDeveloper();

        for (ChangeRequestItem item : cr.getItems()) {
            applierFactory.forAction(item.getAction()).apply(developer, item, approver);
            item.setItemStatus(ItemStatus.APPROVED);
        }

        cr.setReviewedBy(approver);
        cr.setReviewedAt(OffsetDateTime.now());
        cr.setReviewComment(comment);
        cr.setStatus(ChangeRequestStatus.APPROVED);
        auditService.record("CR_APPROVED", "ChangeRequest", cr.getId(), approverId, null, null);
        return mapper.toResponse(cr);
    }

    @Transactional
    public ChangeRequestResponse reject(UUID id, String comment) {
        ChangeRequest cr = reload(id);
        requireStatus(cr, ChangeRequestStatus.SUBMITTED);

        UUID approverId = SecurityUtils.currentUserId();
        cr.setReviewedBy(userRepository.getReferenceById(approverId));
        cr.setReviewedAt(OffsetDateTime.now());
        cr.setReviewComment(comment);
        cr.setStatus(ChangeRequestStatus.REJECTED);
        cr.getItems().forEach(item -> item.setItemStatus(ItemStatus.REJECTED));
        auditService.record("CR_REJECTED", "ChangeRequest", cr.getId(), approverId, null, null);
        return mapper.toResponse(cr);
    }

    @Transactional(readOnly = true)
    public ChangeRequestResponse getById(UUID id) {
        ChangeRequest cr = reload(id);
        if (!SecurityUtils.hasAuthority(PERM_APPROVE) && !isRequester(cr)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Not allowed to view this request");
        }
        return mapper.toResponse(cr);
    }

    @Transactional(readOnly = true)
    public PageResponse<ChangeRequestSummaryResponse> list(Pageable pageable) {
        Page<ChangeRequest> page = SecurityUtils.hasAuthority(PERM_APPROVE)
                ? changeRequestRepository.findAll(pageable)
                : changeRequestRepository.findByRequestedById(SecurityUtils.currentUserId(), pageable);
        return PageResponse.from(page.map(mapper::toSummary));
    }

    private ChangeRequest reload(UUID id) {
        return changeRequestRepository.findWithItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChangeRequest", id));
    }

    private boolean isRequester(ChangeRequest cr) {
        return cr.getRequestedBy().getId().equals(SecurityUtils.currentUserId());
    }

    private void assertRequester(ChangeRequest cr) {
        if (!isRequester(cr)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Only the requester can perform this action");
        }
    }

    private void requireStatus(ChangeRequest cr, ChangeRequestStatus expected) {
        if (cr.getStatus() != expected) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION,
                    "Request must be %s (current: %s)".formatted(expected, cr.getStatus()));
        }
    }
}
