package com.company.resource_management_service.common.web;

import org.springframework.data.domain.Page;

import java.util.List;

/** Serialization-friendly page wrapper (hides Spring's internal Page shape). */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
