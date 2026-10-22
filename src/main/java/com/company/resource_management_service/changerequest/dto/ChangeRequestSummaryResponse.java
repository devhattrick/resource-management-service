package com.company.resource_management_service.changerequest.dto;

import com.company.resource_management_service.changerequest.ChangeRequestStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ChangeRequestSummaryResponse(
        UUID id,
        String developerName,
        String requestedByName,
        ChangeRequestStatus status,
        OffsetDateTime submittedAt) {
}
