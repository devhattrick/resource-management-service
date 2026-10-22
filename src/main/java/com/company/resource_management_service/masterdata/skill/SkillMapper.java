package com.company.resource_management_service.masterdata.skill;

import com.company.resource_management_service.common.exception.ResourceNotFoundException;
import com.company.resource_management_service.common.mapper.EntityMapper;
import com.company.resource_management_service.masterdata.category.SkillCategory;
import com.company.resource_management_service.masterdata.category.SkillCategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper implements EntityMapper<Skill, SkillRequest, SkillResponse> {

    private final SkillCategoryRepository categoryRepository;

    public SkillMapper(SkillCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Skill toEntity(SkillRequest request) {
        Skill entity = new Skill();
        updateEntity(request, entity);
        return entity;
    }

    @Override
    public SkillResponse toResponse(Skill entity) {
        SkillCategory category = entity.getCategory();
        return new SkillResponse(
                entity.getId(),
                category.getId(),
                category.getName(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.isActive());
    }

    @Override
    public void updateEntity(SkillRequest request, Skill target) {
        target.setCategory(resolveCategory(request.categoryId()));
        target.setCode(request.code());
        target.setName(request.name());
        target.setDescription(request.description());
        target.setActive(request.active() == null || request.active());
    }

    private SkillCategory resolveCategory(java.util.UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SkillCategory", categoryId));
    }
}
