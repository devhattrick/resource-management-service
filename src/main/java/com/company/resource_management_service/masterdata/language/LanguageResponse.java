package com.company.resource_management_service.masterdata.language;

import java.util.UUID;

public record LanguageResponse(UUID id, String code, String name, boolean active) {
}
