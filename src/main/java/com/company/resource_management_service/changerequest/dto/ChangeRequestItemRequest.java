package com.company.resource_management_service.changerequest.dto;

import com.company.resource_management_service.changerequest.ChangeAction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/** targetRefId: ADD -> skill_id (master); UPDATE/REMOVE -> developer_skills.id */
public record ChangeRequestItemRequest(
        @NotNull ChangeAction action,
        @NotNull UUID targetRefId,
        @Min(1) @Max(5) Integer proficiencyLevel,
        Boolean mainStack,
        @PositiveOrZero BigDecimal yearsOfExperience,
        LocalDate lastUsedDate) {
}
