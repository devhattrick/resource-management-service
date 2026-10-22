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
import com.company.resource_management_service.masterdata.skill.Skill;
import com.company.resource_management_service.masterdata.skill.SkillRepository;
import com.company.resource_management_service.user.entity.AppUser;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AddSkillApplier implements SkillChangeApplier {

    private final DeveloperSkillRepository developerSkillRepository;
    private final SkillRepository skillRepository;
    private final AuditService auditService;

    public AddSkillApplier(DeveloperSkillRepository developerSkillRepository,
                           SkillRepository skillRepository,
                           AuditService auditService) {
        this.developerSkillRepository = developerSkillRepository;
        this.skillRepository = skillRepository;
        this.auditService = auditService;
    }

    @Override
    public ChangeAction action() {
        return ChangeAction.ADD;
    }

    @Override
    public void apply(DeveloperProfile developer, ChangeRequestItem item, AppUser approver) {
        SkillChangePayload payload = item.getPayload();
        if (payload.getProficiencyLevel() == null) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, "proficiencyLevel is required to add a skill");
        }
        java.util.UUID skillId = item.getTargetRefId();
        if (developerSkillRepository.existsByDeveloperIdAndSkillId(developer.getId(), skillId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "Developer already has this skill");
        }
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", skillId));

        DeveloperSkill ds = new DeveloperSkill();
        ds.setDeveloper(developer);
        ds.setSkill(skill);
        ds.setProficiencyLevel(payload.getProficiencyLevel());
        ds.setMainStack(Boolean.TRUE.equals(payload.getMainStack()));
        ds.setYearsOfExperience(payload.getYearsOfExperience());
        ds.setLastUsedDate(payload.getLastUsedDate());
        ds.setApprovedBy(approver);
        ds.setApprovedAt(OffsetDateTime.now());
        ds.setSourceChangeRequestId(item.getChangeRequest().getId());
        DeveloperSkill saved = developerSkillRepository.save(ds);

        Map<String, Object> after = new HashMap<>();
        after.put("skillId", skillId.toString());
        after.put("proficiencyLevel", ds.getProficiencyLevel());
        after.put("mainStack", ds.isMainStack());
        auditService.record("SKILL_ADDED", "DeveloperSkill", saved.getId(),
                approver.getId(), null, after);
    }
}
