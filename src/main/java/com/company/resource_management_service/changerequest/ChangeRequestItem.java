package com.company.resource_management_service.changerequest;

import com.company.resource_management_service.common.entity.BaseIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "change_request_items")
@Getter
@Setter
public class ChangeRequestItem extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "change_request_id", nullable = false)
    private ChangeRequest changeRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private ChangeTargetType targetType = ChangeTargetType.SKILL;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ChangeAction action;

    /** ADD -> skill_id (master); UPDATE/REMOVE -> developer_skills.id */
    @Column(name = "target_ref_id")
    private UUID targetRefId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false)
    private SkillChangePayload payload = new SkillChangePayload();

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    private ItemStatus itemStatus = ItemStatus.PENDING;
}
