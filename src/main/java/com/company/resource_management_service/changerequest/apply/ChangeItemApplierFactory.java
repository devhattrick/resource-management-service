package com.company.resource_management_service.changerequest.apply;

import com.company.resource_management_service.changerequest.ChangeAction;
import com.company.resource_management_service.common.exception.BusinessException;
import com.company.resource_management_service.common.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Factory: resolves the right {@link SkillChangeApplier} for an action. */
@Component
public class ChangeItemApplierFactory {

    private final Map<ChangeAction, SkillChangeApplier> appliers = new EnumMap<>(ChangeAction.class);

    public ChangeItemApplierFactory(List<SkillChangeApplier> applierList) {
        for (SkillChangeApplier applier : applierList) {
            appliers.put(applier.action(), applier);
        }
    }

    public SkillChangeApplier forAction(ChangeAction action) {
        SkillChangeApplier applier = appliers.get(action);
        if (applier == null) {
            throw new BusinessException(ErrorCode.BUSINESS_RULE_VIOLATION, "Unsupported change action: " + action);
        }
        return applier;
    }
}
