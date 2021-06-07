package com.light;

import com.light.candidate.repository.CandidateRepository;
import com.light.repository.LanguageRepository;
import com.light.scribe.repository.ScribeRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
