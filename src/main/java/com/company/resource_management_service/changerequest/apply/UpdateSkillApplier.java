package com.company.resource_management_service.changerequest.apply;

import com.company.resource_management_service.audit.AuditService;
import com.company.resource_management_service.changerequest.ChangeAction;
import com.company.resource_management_service.changerequest.ChangeRequestItem;
import com.company.resource_management_service.changerequest.SkillChangePayload;
import com.company.resource_management_service.common.exception.BusinessException;
import com.company.resource_management_service.common.exception.ErrorCode;
import com.company.resource_management_service.common.exception.ResourceNotFoundException;
import com.company.resource_management_service.developer.DeveloperProfile;
import com.company.resource_management_service.developer.DeveloperSkill;
import com.company.resource_management_service.developer.DeveloperSkillRepository;
import com.company.resource_management_service.user.entity.AppUser;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UpdateSkillApplier implements SkillChangeApplier {

    private final DeveloperSkillRepository developerSkillRepository;
    private final AuditService auditService;

    public UpdateSkillApplier(DeveloperSkillRepository developerSkillRepository, AuditService auditService) {
        this.developerSkillRepository = developerSkillRepository;
        this.auditService = auditService;
    }

    @Override
    public ChangeAction action() {
        return ChangeAction.UPDATE;
    }

    @Override
    public void apply(DeveloperProfile developer, ChangeRequestItem item, AppUser approver) {
        DeveloperSkill ds = loadOwnedSkill(developer, item.getTargetRefId());
        SkillChangePayload payload = item.getPayload();

        Map<String, Object> before = new HashMap<>();
        before.put("proficiencyLevel", ds.getProficiencyLevel());
        before.put("mainStack", ds.isMainStack());

        if (payload.getProficiencyLevel() != null) {
            ds.setProficiencyLevel(payload.getProficiencyLevel());
        }
        if (payload.getMainStack() != null) {
            ds.setMainStack(payload.getMainStack());
        }
        if (payload.getYearsOfExperience() != null) {
            ds.setYearsOfExperience(payload.getYearsOfExperience());
        }
        if (payload.getLastUsedDate() != null) {
            ds.setLastUsedDate(payload.getLastUsedDate());
        }
        ds.setApprovedBy(approver);
        ds.setApprovedAt(OffsetDateTime.now());

        Map<String, Object> after = new HashMap<>();
        after.put("proficiencyLevel", ds.getProficiencyLevel());
        after.put("mainStack", ds.isMainStack());
        auditService.record("SKILL_UPDATED", "DeveloperSkill", ds.getId(),
                approver.getId(), before, after);
    }

    private DeveloperSkill loadOwnedSkill(DeveloperProfile developer, UUID developerSkillId) {
        DeveloperSkill ds = developerSkillRepository.findById(developerSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("DeveloperSkill", developerSkillId));
        if (!ds.getDeveloper().getId().equals(developer.getId())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION,
                    "Target skill does not belong to this developer");
        }
        return ds;
    }
}
