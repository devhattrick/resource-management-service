package com.company.resource_management_service.developer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeveloperLanguageRequest(
        @NotNull UUID languageId,
        @NotNull @Min(1) @Max(5) Integer listeningLevel,
        @NotNull @Min(1) @Max(5) Integer speakingLevel,
        @NotNull @Min(1) @Max(5) Integer readingLevel,
        @NotNull @Min(1) @Max(5) Integer writingLevel) {
}
