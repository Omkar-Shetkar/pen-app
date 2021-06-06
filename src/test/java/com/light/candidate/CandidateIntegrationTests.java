package com.light.candidate;

import com.light.BaseTestSetup;
import com.light.candidate.repository.CandidateRepository;
import com.light.scribe.repository.ScribeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CandidateIntegrationTests extends BaseTestSetup {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ScribeRepository scribeRepository;

    @Test
    void testAllCandidates() {
        long totalCandidates = candidateRepository.count();
        Assertions.assertEquals(3, totalCandidates);
    }
}
