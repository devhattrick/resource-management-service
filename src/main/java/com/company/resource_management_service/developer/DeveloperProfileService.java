package com.company.resource_management_service.developer;

import com.company.resource_management_service.common.exception.BusinessException;
import com.company.resource_management_service.common.exception.ErrorCode;
import com.company.resource_management_service.common.exception.ResourceNotFoundException;
import com.company.resource_management_service.common.web.PageResponse;
import com.company.resource_management_service.developer.dto.DeveloperLanguageRequest;
import com.company.resource_management_service.developer.dto.DeveloperProfileCreateRequest;
import com.company.resource_management_service.developer.dto.DeveloperProfileResponse;
import com.company.resource_management_service.developer.dto.DeveloperProfileUpdateRequest;
import com.company.resource_management_service.developer.dto.DeveloperRoleRequest;
import com.company.resource_management_service.developer.dto.DeveloperSummaryResponse;
import com.company.resource_management_service.masterdata.language.Language;
import com.company.resource_management_service.masterdata.language.LanguageRepository;
import com.company.resource_management_service.masterdata.jobrole.JobRole;
import com.company.resource_management_service.masterdata.jobrole.JobRoleRepository;
import com.company.resource_management_service.security.SecurityUtils;
import com.company.resource_management_service.user.entity.AppUser;
import com.company.resource_management_service.user.repository.AppUserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class DeveloperProfileService {

    private static final String PERM_MANAGE = "PROFILE_MANAGE";

    private final DeveloperProfileRepository profileRepository;
    private final AppUserRepository userRepository;
    private final JobRoleRepository jobRoleRepository;
    private final LanguageRepository languageRepository;
    private final DeveloperProfileMapper mapper;

    public DeveloperProfileService(DeveloperProfileRepository profileRepository,
                                   AppUserRepository userRepository,
                                   JobRoleRepository jobRoleRepository,
                                   LanguageRepository languageRepository,
                                   DeveloperProfileMapper mapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.jobRoleRepository = jobRoleRepository;
        this.languageRepository = languageRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<DeveloperSummaryResponse> list(Pageable pageable) {
        return PageResponse.from(profileRepository.findAll(pageable).map(mapper::toSummary));
    }

    @Transactional(readOnly = true)
    public DeveloperProfileResponse getById(UUID id) {
        return mapper.toResponse(loadDetailed(id));
    }

    @Transactional
    public DeveloperProfileResponse create(DeveloperProfileCreateRequest request) {
        if (profileRepository.existsByUserId(request.userId())) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "Developer profile already exists for this user");
        }
        AppUser user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", request.userId()));

        DeveloperProfile profile = new DeveloperProfile();
        profile.setUser(user);
        profile.setSeniority(request.seniority());
        profile.setTotalExperienceYears(
                request.totalExperienceYears() == null ? BigDecimal.ZERO : request.totalExperienceYears());
        profile.setCapacityPercent(request.capacityPercent() == null ? 100 : request.capacityPercent());
        profile.setProfileStatus(ProfileStatus.ACTIVE);
        profile.setNote(request.note());

        DeveloperProfile saved = profileRepository.save(profile);
        return mapper.toResponse(loadDetailed(saved.getId()));
    }

    @Transactional
    public DeveloperProfileResponse updateCore(UUID id, DeveloperProfileUpdateRequest request) {
        DeveloperProfile profile = loadDetailed(id);
        assertCanEdit(profile);
        mapper.applyCore(request, profile);
        return mapper.toResponse(profile);
    }

    @Transactional
    public DeveloperProfileResponse replaceRoles(UUID id, List<DeveloperRoleRequest> roles) {
        DeveloperProfile profile = loadDetailed(id);
        assertCanEdit(profile);
        profile.getRoles().clear();
        for (DeveloperRoleRequest r : roles) {
            JobRole jobRole = jobRoleRepository.findById(r.jobRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("JobRole", r.jobRoleId()));
            DeveloperRole dr = new DeveloperRole();
            dr.setJobRole(jobRole);
            dr.setPrimary(Boolean.TRUE.equals(r.primary()));
            profile.addRole(dr);
        }
        return mapper.toResponse(profile);
    }

    @Transactional
    public DeveloperProfileResponse replaceLanguages(UUID id, List<DeveloperLanguageRequest> languages) {
        DeveloperProfile profile = loadDetailed(id);
        assertCanEdit(profile);
        profile.getLanguages().clear();
        for (DeveloperLanguageRequest l : languages) {
            Language language = languageRepository.findById(l.languageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Language", l.languageId()));
            DeveloperLanguage dl = new DeveloperLanguage();
            dl.setLanguage(language);
            dl.setListeningLevel(l.listeningLevel());
            dl.setSpeakingLevel(l.speakingLevel());
            dl.setReadingLevel(l.readingLevel());
            dl.setWritingLevel(l.writingLevel());
            profile.addLanguage(dl);
        }
        return mapper.toResponse(profile);
    }

    private DeveloperProfile loadDetailed(UUID id) {
        return profileRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeveloperProfile", id));
    }

    /** Owner may edit own profile; PROFILE_MANAGE may edit anyone's. */
    private void assertCanEdit(DeveloperProfile profile) {
        if (SecurityUtils.hasAuthority(PERM_MANAGE)) {
            return;
        }
        if (!profile.getUser().getId().equals(SecurityUtils.currentUserId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "You can only edit your own profile");
        }
    }
}
