package com.company.resource_management_service.developer;

import com.company.resource_management_service.common.entity.BaseIdEntity;
import com.company.resource_management_service.masterdata.language.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "developer_languages")
@Getter
@Setter
public class DeveloperLanguage extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "developer_id", nullable = false)
    private DeveloperProfile developer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "listening_level", nullable = false)
    private int listeningLevel;

    @Column(name = "speaking_level", nullable = false)
    private int speakingLevel;

    @Column(name = "reading_level", nullable = false)
    private int readingLevel;

    @Column(name = "writing_level", nullable = false)
    private int writingLevel;
}
