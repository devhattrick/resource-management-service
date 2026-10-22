package com.company.resource_management_service.masterdata.language;

import com.company.resource_management_service.common.service.BaseCrudService;
import com.company.resource_management_service.common.web.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/languages")
public class LanguageController extends BaseCrudController<LanguageRequest, LanguageResponse> {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Override
    protected BaseCrudService<?, LanguageRequest, LanguageResponse> service() {
        return languageService;
    }
}
