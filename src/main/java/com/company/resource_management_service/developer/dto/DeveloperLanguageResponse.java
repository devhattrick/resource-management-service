package com.company.resource_management_service.developer.dto;

import java.util.UUID;

public record DeveloperLanguageResponse(
        UUID languageId,
        String languageCode,
        String languageName,
        int listeningLevel,
        int speakingLevel,
        int readingLevel,
        int writingLevel) {
}
