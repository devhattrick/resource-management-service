package com.company.resource_management_service.developer;

import com.company.resource_management_service.developer.dto.DeveloperLanguageResponse;
import com.company.resource_management_service.developer.dto.DeveloperProfileResponse;
import com.company.resource_management_service.developer.dto.DeveloperProfileUpdateRequest;
import com.company.resource_management_service.developer.dto.DeveloperRoleResponse;
import com.company.resource_management_service.developer.dto.DeveloperSummaryResponse;
import com.company.resource_management_service.user.entity.AppUser;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/** Maps DeveloperProfile aggregates to DTOs and applies core-field updates. */
@Component
public class DeveloperProfileMapper {

    public DeveloperProfileResponse toResponse(DeveloperProfile p) {
        AppUser user = p.getUser();
        List<DeveloperRoleResponse> roles = p.getRoles().stream()
                .map(this::toRoleResponse)
                .sorted(Comparator.comparing(DeveloperRoleResponse::jobRoleName))
                .toList();
        List<DeveloperLanguageResponse> languages = p.getLanguages().stream()
                .map(this::toLanguageResponse)
                .sorted(Comparator.comparing(DeveloperLanguageResponse::languageName))
                .toList();

        return new DeveloperProfileResponse(
                p.getId(),
                user.getId(),
                user.getEmployeeCode(),
                user.getEmail(),
                user.getFullName(),
                p.getSeniority(),
                p.getTotalExperienceYears(),
                p.getCapacityPercent(),
                p.getProfileStatus(),
                p.getNote(),
                roles,
                languages);
    }

    public DeveloperSummaryResponse toSummary(DeveloperProfile p) {
        AppUser user = p.getUser();
        return new DeveloperSummaryResponse(
                p.getId(),
                user.getId(),
                user.getEmployeeCode(),
                user.getFullName(),
                p.getSeniority(),
                p.getCapacityPercent(),
                p.getProfileStatus());
    }

    public void applyCore(DeveloperProfileUpdateRequest request, DeveloperProfile target) {
        target.setSeniority(request.seniority());
        target.setTotalExperienceYears(request.totalExperienceYears());
        target.setCapacityPercent(request.capacityPercent());
        target.setProfileStatus(request.profileStatus());
        target.setNote(request.note());
    }

    private DeveloperRoleResponse toRoleResponse(DeveloperRole r) {
        return new DeveloperRoleResponse(
                r.getJobRole().getId(),
                r.getJobRole().getCode(),
                r.getJobRole().getName(),
                r.isPrimary());
    }

    private DeveloperLanguageResponse toLanguageResponse(DeveloperLanguage l) {
        return new DeveloperLanguageResponse(
                l.getLanguage().getId(),
                l.getLanguage().getCode(),
                l.getLanguage().getName(),
                l.getListeningLevel(),
                l.getSpeakingLevel(),
                l.getReadingLevel(),
                l.getWritingLevel());
    }
}
