package com.pen;

import com.pen.candidate.repository.CandidateRepository;
import com.pen.exam.repository.ExamEventRepository;
import com.pen.language.repository.LanguageRepository;
import com.pen.request.repository.ExamScheduleRepository;
import com.pen.request.repository.RequestRepository;
import com.pen.scribe.repository.ScribeRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser
public abstract class BaseTestSetup {

    @Autowired
    private ScribeRepository scribeRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private ExamEventRepository examEventRepository;

    @AfterEach
    void cleanUpData() {
        examEventRepository.deleteAll();
        requestRepository.deleteAll();
        examScheduleRepository.deleteAll();
        scribeRepository.deleteAll();
        languageRepository.deleteAll();
        candidateRepository.deleteAll();
    }

}
