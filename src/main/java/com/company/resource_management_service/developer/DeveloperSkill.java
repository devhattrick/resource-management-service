package com.company.resource_management_service.developer;

import com.company.resource_management_service.common.entity.BaseEntity;
import com.company.resource_management_service.masterdata.skill.Skill;
import com.company.resource_management_service.user.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/** A skill that has been APPROVED onto a developer's profile (level 1-5). */
@Entity
@Table(name = "developer_skills")
@Getter
@Setter
public class DeveloperSkill extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "developer_id", nullable = false)
    private DeveloperProfile developer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "proficiency_level", nullable = false)
    private int proficiencyLevel;

    @Column(name = "is_main_stack", nullable = false)
    private boolean mainStack = false;

    @Column(name = "years_of_experience")
    private BigDecimal yearsOfExperience;

    @Column(name = "last_used_date")
    private LocalDate lastUsedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private AppUser approvedBy;

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    @Column(name = "source_change_request_id")
    private UUID sourceChangeRequestId;
}
