package com.company.resource_management_service.masterdata.category;

import com.company.resource_management_service.common.mapper.EntityMapper;
import org.springframework.stereotype.Component;

@Component
public class SkillCategoryMapper
        implements EntityMapper<SkillCategory, SkillCategoryRequest, SkillCategoryResponse> {

    @Override
    public SkillCategory toEntity(SkillCategoryRequest request) {
        SkillCategory entity = new SkillCategory();
        updateEntity(request, entity);
        return entity;
    }

    @Override
    public SkillCategoryResponse toResponse(SkillCategory entity) {
        return new SkillCategoryResponse(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getSortOrder(),
                entity.isActive());
    }

    @Override
    public void updateEntity(SkillCategoryRequest request, SkillCategory target) {
        target.setCode(request.code());
        target.setName(request.name());
        target.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        target.setActive(request.active() == null || request.active());
    }
}
