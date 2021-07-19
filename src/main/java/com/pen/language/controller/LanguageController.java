package com.pen.language.controller;

import com.pen.domain.Language;
import com.pen.language.repository.LanguageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/languages")
public class LanguageController {

    public final static String ALL = "/rest/languages";
    private final LanguageRepository languageRepository;

    public LanguageController(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Language>> getLanguages() {
        Iterable<Language> languages = languageRepository.findAll();
        return ResponseEntity.ok(languages);
    }
}
