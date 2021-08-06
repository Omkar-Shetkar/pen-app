package com.pen.candidate.service;

import com.pen.candidate.domain.Candidate;
import com.pen.candidate.repository.CandidateRepository;
import com.pen.client.register.UserResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find candidate id: " + candidateId));
        log.info("Candidate info found for candidateId {}",candidateId);
        return candidate;
    }

    public Candidate create(String userId, UserResource user) {
        Candidate candidate = new Candidate(userId, user.getFirstName(), user.getLastName(),
                user.getGender(), user.getMobile(), user.getEmail());
        log.info("New candidate created successfully with id{}",candidate.getId());
        return candidateRepository.insert(candidate);
    }

}
