package com.company.resource_management_service.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Lightweight base for reference/master tables that carry only a surrogate key
 * (no audit timestamps or optimistic-lock version).
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
