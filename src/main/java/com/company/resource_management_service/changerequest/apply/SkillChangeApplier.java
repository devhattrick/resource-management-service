package com.company.resource_management_service.changerequest.apply;

import com.company.resource_management_service.changerequest.ChangeAction;
import com.company.resource_management_service.changerequest.ChangeRequestItem;
import com.company.resource_management_service.developer.DeveloperProfile;
import com.company.resource_management_service.user.entity.AppUser;

/**
 * Strategy: applies one change-request item to a developer's approved skills.
 * One implementation per {@link ChangeAction}; selected by {@link ChangeItemApplierFactory}.
 */
public interface SkillChangeApplier {

    ChangeAction action();

    void apply(DeveloperProfile developer, ChangeRequestItem item, AppUser approver);
}
