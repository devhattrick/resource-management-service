package com.company.resource_management_service.changerequest;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Proposed values carried in change_request_items.payload (JSONB). No skill name here (closed catalog). */
@Getter
@Setter
public class SkillChangePayload {

    private Integer proficiencyLevel;
    private Boolean mainStack;
    private BigDecimal yearsOfExperience;
    private LocalDate lastUsedDate;
}
