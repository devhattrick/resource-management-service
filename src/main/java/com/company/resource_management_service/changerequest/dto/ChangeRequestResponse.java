package com.company.resource_management_service.changerequest.dto;

import com.company.resource_management_service.changerequest.ChangeRequestStatus;
import com.company.resource_management_service.changerequest.RequestType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ChangeRequestResponse(
        UUID id,
        UUID developerId,
        String developerName,
        UUID requestedById,
        String requestedByName,
        RequestType requestType,
        ChangeRequestStatus status,
        String note,
        String reviewedByName,
        OffsetDateTime reviewedAt,
        String reviewComment,
        OffsetDateTime submittedAt,
        List<ChangeRequestItemResponse> items) {
}
