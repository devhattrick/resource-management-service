package com.company.resource_management_service.masterdata.language;

import com.company.resource_management_service.common.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class LanguageService extends BaseCrudService<Language, LanguageRequest, LanguageResponse> {

    public LanguageService(LanguageRepository repository, LanguageMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String entityName() {
        return "Language";
    }
}
