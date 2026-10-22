package com.company.resource_management_service.masterdata.jobrole;

import com.company.resource_management_service.common.mapper.EntityMapper;
import org.springframework.stereotype.Component;

@Component
public class JobRoleMapper implements EntityMapper<JobRole, JobRoleRequest, JobRoleResponse> {

    @Override
    public JobRole toEntity(JobRoleRequest request) {
        JobRole entity = new JobRole();
        updateEntity(request, entity);
        return entity;
    }

    @Override
    public JobRoleResponse toResponse(JobRole entity) {
        return new JobRoleResponse(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.isActive());
    }

    @Override
    public void updateEntity(JobRoleRequest request, JobRole target) {
        target.setCode(request.code());
        target.setName(request.name());
        target.setDescription(request.description());
        target.setActive(request.active() == null || request.active());
    }
}
