package com.light;

import com.light.candidate.repository.CandidateRepository;
import com.light.repository.LanguageRepository;
import com.light.scribe.repository.ScribeRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseTestSetup {

    @Autowired
    private ScribeRepository scribeRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @AfterEach
    void cleanUpData() {
        scribeRepository.deleteAll();
        languageRepository.deleteAll();
        candidateRepository.deleteAll();
    }

}
