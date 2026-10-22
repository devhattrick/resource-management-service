package com.company.resource_management_service.masterdata.skill;

import com.company.resource_management_service.common.service.BaseCrudService;
import com.company.resource_management_service.common.web.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
public class SkillController extends BaseCrudController<SkillRequest, SkillResponse> {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @Override
    protected BaseCrudService<?, SkillRequest, SkillResponse> service() {
        return skillService;
    }
}
