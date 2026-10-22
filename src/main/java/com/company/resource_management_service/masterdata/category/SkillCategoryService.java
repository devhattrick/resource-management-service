package com.company.resource_management_service.masterdata.category;

import com.company.resource_management_service.common.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class SkillCategoryService
        extends BaseCrudService<SkillCategory, SkillCategoryRequest, SkillCategoryResponse> {

    public SkillCategoryService(SkillCategoryRepository repository, SkillCategoryMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String entityName() {
        return "SkillCategory";
    }
}
