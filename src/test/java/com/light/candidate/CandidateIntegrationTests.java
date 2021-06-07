package com.light.candidate;

import com.light.BaseTestSetup;
import com.light.candidate.repository.CandidateRepository;
import com.light.scribe.repository.ScribeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CandidateIntegrationTests extends BaseTestSetup {

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
