package com.light.candidate;

import com.light.candidate.repository.CandidateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CandidateIntegrationTests {

    @Autowired
    private CandidateRepository candidateRepository;

//    @Autowired
//    private ScribeRepository scribeRepository;


    @Test
    void testAllCandidates() {
        long totalCandidates = candidateRepository.count();
        Assertions.assertEquals(3, totalCandidates);
    }
}
