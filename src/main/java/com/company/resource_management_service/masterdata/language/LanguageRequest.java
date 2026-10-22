package com.company.resource_management_service.masterdata.language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LanguageRequest(
        @NotBlank @Size(max = 10) String code,
        @NotBlank @Size(max = 50) String name,
        Boolean active) {
}
