package com.company.resource_management_service.changerequest;

import com.company.resource_management_service.changerequest.dto.ChangeRequestItemResponse;
import com.company.resource_management_service.changerequest.dto.ChangeRequestResponse;
import com.company.resource_management_service.changerequest.dto.ChangeRequestSummaryResponse;
import com.company.resource_management_service.developer.DeveloperProfile;
import com.company.resource_management_service.user.entity.AppUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChangeRequestMapper {

    public ChangeRequestResponse toResponse(ChangeRequest cr) {
        DeveloperProfile dev = cr.getDeveloper();
        AppUser requester = cr.getRequestedBy();
        AppUser reviewer = cr.getReviewedBy();
        List<ChangeRequestItemResponse> items = cr.getItems().stream().map(this::toItem).toList();

        return new ChangeRequestResponse(
                cr.getId(),
                dev.getId(),
                dev.getUser().getFullName(),
                requester.getId(),
                requester.getFullName(),
                cr.getRequestType(),
                cr.getStatus(),
                cr.getNote(),
                reviewer == null ? null : reviewer.getFullName(),
                cr.getReviewedAt(),
                cr.getReviewComment(),
                cr.getSubmittedAt(),
                items);
    }

    public ChangeRequestSummaryResponse toSummary(ChangeRequest cr) {
        return new ChangeRequestSummaryResponse(
                cr.getId(),
                cr.getDeveloper().getUser().getFullName(),
                cr.getRequestedBy().getFullName(),
                cr.getStatus(),
                cr.getSubmittedAt());
    }

    private ChangeRequestItemResponse toItem(ChangeRequestItem i) {
        SkillChangePayload p = i.getPayload();
        return new ChangeRequestItemResponse(
                i.getId(),
                i.getTargetType(),
                i.getAction(),
                i.getTargetRefId(),
                p.getProficiencyLevel(),
                p.getMainStack(),
                p.getYearsOfExperience(),
                p.getLastUsedDate(),
                i.getItemStatus());
    }
}
