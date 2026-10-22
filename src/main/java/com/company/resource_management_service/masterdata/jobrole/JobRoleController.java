package com.company.resource_management_service.masterdata.jobrole;

import com.company.resource_management_service.common.service.BaseCrudService;
import com.company.resource_management_service.common.web.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-roles")
public class JobRoleController extends BaseCrudController<JobRoleRequest, JobRoleResponse> {

    private final JobRoleService jobRoleService;

    public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }

    @Override
    protected BaseCrudService<?, JobRoleRequest, JobRoleResponse> service() {
        return jobRoleService;
    }
}
