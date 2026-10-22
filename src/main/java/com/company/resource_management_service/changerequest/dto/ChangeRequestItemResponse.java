package com.company.resource_management_service.changerequest.dto;

import com.company.resource_management_service.changerequest.ChangeAction;
import com.company.resource_management_service.changerequest.ChangeTargetType;
import com.company.resource_management_service.changerequest.ItemStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ChangeRequestItemResponse(
        UUID id,
        ChangeTargetType targetType,
        ChangeAction action,
        UUID targetRefId,
        Integer proficiencyLevel,
        Boolean mainStack,
        BigDecimal yearsOfExperience,
        LocalDate lastUsedDate,
        ItemStatus itemStatus) {
}
