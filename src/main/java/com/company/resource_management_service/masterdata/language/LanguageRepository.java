package com.company.resource_management_service.masterdata.language;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LanguageRepository extends JpaRepository<Language, UUID> {
    boolean existsByCode(String code);
}
