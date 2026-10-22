package com.company.resource_management_service.masterdata.jobrole;

import com.company.resource_management_service.common.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class JobRoleService extends BaseCrudService<JobRole, JobRoleRequest, JobRoleResponse> {

    public JobRoleService(JobRoleRepository repository, JobRoleMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String entityName() {
        return "JobRole";
    }
}
