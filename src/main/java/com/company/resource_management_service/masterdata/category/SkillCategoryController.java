package com.company.resource_management_service.masterdata.category;

import com.company.resource_management_service.common.service.BaseCrudService;
import com.company.resource_management_service.common.web.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skill-categories")
public class SkillCategoryController
        extends BaseCrudController<SkillCategoryRequest, SkillCategoryResponse> {

    private final SkillCategoryService skillCategoryService;

    public SkillCategoryController(SkillCategoryService skillCategoryService) {
        this.skillCategoryService = skillCategoryService;
    }

    @Override
    protected BaseCrudService<?, SkillCategoryRequest, SkillCategoryResponse> service() {
        return skillCategoryService;
    }
}
