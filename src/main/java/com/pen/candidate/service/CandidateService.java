package com.pen.candidate.service;

import com.pen.candidate.domain.Candidate;
import com.pen.candidate.repository.CandidateRepository;
import com.pen.client.register.UserResource;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository){
        this.candidateRepository = candidateRepository;
    }

    /**
     * @param candidateId
     * @return candidate
     * @throws IllegalArgumentException - For invalid candidate id
     */
    public Candidate findById(String candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find candidate id: " + candidateId));
    }

    public Candidate create(String userId, UserResource user) {
        Candidate candidate = new Candidate(userId, user.getFirstName(), user.getLastName(),
                user.getGender(), user.getMobile(), user.getEmail());
        return candidateRepository.insert(candidate);
    }

}
