package com.company.resource_management_service.masterdata.language;

import com.company.resource_management_service.common.mapper.EntityMapper;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper implements EntityMapper<Language, LanguageRequest, LanguageResponse> {

    @Override
    public Language toEntity(LanguageRequest request) {
        Language entity = new Language();
        updateEntity(request, entity);
        return entity;
    }

    @Override
    public LanguageResponse toResponse(Language entity) {
        return new LanguageResponse(entity.getId(), entity.getCode(), entity.getName(), entity.isActive());
    }

    @Override
    public void updateEntity(LanguageRequest request, Language target) {
        target.setCode(request.code());
        target.setName(request.name());
        target.setActive(request.active() == null || request.active());
    }
}
