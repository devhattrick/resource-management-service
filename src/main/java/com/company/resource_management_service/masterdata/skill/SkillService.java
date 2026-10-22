package com.company.resource_management_service.masterdata.skill;

import com.company.resource_management_service.common.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class SkillService extends BaseCrudService<Skill, SkillRequest, SkillResponse> {

    public SkillService(SkillRepository repository, SkillMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String entityName() {
        return "Skill";
    }
}
