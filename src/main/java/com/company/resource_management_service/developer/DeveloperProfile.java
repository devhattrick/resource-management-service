package com.company.resource_management_service.developer;

import com.company.resource_management_service.common.entity.BaseEntity;
import com.company.resource_management_service.user.entity.AppUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "developer_profiles")
@Getter
@Setter
public class DeveloperProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "seniority")
    private Seniority seniority;

    @Column(name = "total_experience_years", nullable = false)
    private BigDecimal totalExperienceYears = BigDecimal.ZERO;

    @Column(name = "capacity_percent", nullable = false)
    private int capacityPercent = 100;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status", nullable = false)
    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeveloperRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeveloperLanguage> languages = new HashSet<>();

    public void addRole(DeveloperRole role) {
        role.setDeveloper(this);
        roles.add(role);
    }

    public void addLanguage(DeveloperLanguage language) {
        language.setDeveloper(this);
        languages.add(language);
    }
}
