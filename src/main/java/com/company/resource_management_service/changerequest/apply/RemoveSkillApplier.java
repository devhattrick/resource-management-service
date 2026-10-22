package com.company.resource_management_service.changerequest.apply;

import com.company.resource_management_service.audit.AuditService;
import com.company.resource_management_service.changerequest.ChangeAction;
import com.company.resource_management_service.changerequest.ChangeRequestItem;
import com.company.resource_management_service.common.exception.BusinessException;
import com.company.resource_management_service.common.exception.ErrorCode;
import com.company.resource_management_service.common.exception.ResourceNotFoundException;
import com.company.resource_management_service.developer.DeveloperProfile;
import com.company.resource_management_service.developer.DeveloperSkill;
import com.company.resource_management_service.developer.DeveloperSkillRepository;
import com.company.resource_management_service.user.entity.AppUser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RemoveSkillApplier implements SkillChangeApplier {

    private final DeveloperSkillRepository developerSkillRepository;
    private final AuditService auditService;

    public RemoveSkillApplier(DeveloperSkillRepository developerSkillRepository, AuditService auditService) {
        this.developerSkillRepository = developerSkillRepository;
        this.auditService = auditService;
    }

    @Override
    public ChangeAction action() {
        return ChangeAction.REMOVE;
    }

    @Override
    public void apply(DeveloperProfile developer, ChangeRequestItem item, AppUser approver) {
        UUID developerSkillId = item.getTargetRefId();
        DeveloperSkill ds = developerSkillRepository.findById(developerSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("DeveloperSkill", developerSkillId));
        if (!ds.getDeveloper().getId().equals(developer.getId())) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION,
                    "Target skill does not belong to this developer");
        }

        Map<String, Object> before = new HashMap<>();
        before.put("skillId", ds.getSkill().getId().toString());
        before.put("proficiencyLevel", ds.getProficiencyLevel());

        developerSkillRepository.delete(ds);
        auditService.record("SKILL_REMOVED", "DeveloperSkill", developerSkillId,
                approver.getId(), before, null);
    }
}
