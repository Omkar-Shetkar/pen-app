package com.light.candidate.service;

import com.light.candidate.domain.Candidate;
import com.light.candidate.repository.CandidateRepository;
import com.light.client.register.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    /**
     * @param candidateId
     * @return
     * @throws IllegalArgumentException - For invalid candidate id
     */
    public Candidate findById(String candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find candidate id: " + candidateId));
    }

    public void create(String userId, UserResource user) {
        Candidate candidate = new Candidate(userId, user.getFirstName(), user.getLastName(),
                user.getGender(), user.getPhone());
        candidateRepository.insert(candidate);
    }

}
