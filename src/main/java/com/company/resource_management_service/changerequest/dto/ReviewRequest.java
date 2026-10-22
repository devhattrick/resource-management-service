package com.company.resource_management_service.changerequest.dto;

import jakarta.validation.constraints.Size;

public record ReviewRequest(@Size(max = 2000) String comment) {
}
