package com.light.candidate.service;

import com.light.candidate.domain.Candidate;
import com.light.candidate.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    /**
     *
     * @param candidateId
     * @return
     * @throws IllegalArgumentException - For invalid candidate id
     */
    public Candidate findById(long candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find candidate id: " + candidateId));
    }
}
