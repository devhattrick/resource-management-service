package com.company.resource_management_service.masterdata.jobrole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JobRoleRequest(
        @NotBlank @Size(max = 30) String code,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 2000) String description,
        Boolean active) {
}
