package com.company.resource_management_service.audit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

/** Thin helper so any service can append an audit record in one call. */
@Service
public class AuditService {

    private final AuditEventRepository repository;

    public AuditService(AuditEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void record(String eventType, String entityType, UUID entityId, UUID actorUserId,
                       Map<String, Object> before, Map<String, Object> after) {
        AuditEvent event = new AuditEvent();
        event.setEventType(eventType);
        event.setEntityType(entityType);
        event.setEntityId(entityId);
        event.setActorUserId(actorUserId);
        event.setBeforeData(before);
        event.setAfterData(after);
        repository.save(event);
    }
}
