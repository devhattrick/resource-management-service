package com.company.resource_management_service.changerequest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateChangeRequestRequest(
        @Size(max = 2000) String note,
        @NotEmpty @Valid List<ChangeRequestItemRequest> items) {
}
