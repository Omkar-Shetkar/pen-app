package com.light.user.service;

import com.light.candidate.domain.Candidate;
import com.light.candidate.repository.CandidateRepository;
import com.light.client.exam.UserType;
import com.light.client.register.UserResource;
import com.light.scribe.domain.Scribe;
import com.light.scribe.repository.ScribeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private ScribeRepository scribeRepository;
    private CandidateRepository candidateRepository;

    public UserService(ScribeRepository scribeRepository, CandidateRepository candidateRepository) {
        this.scribeRepository = scribeRepository;
        this.candidateRepository = candidateRepository;
    }

    public UserResource getUser(String userId) {
        Optional<UserResource> userResource = scribeRepository.findById(userId).map(s -> s.asUserResource());
        if (userResource.isEmpty()) {
            userResource = candidateRepository.findById(userId).map(c -> c.asUserResource());
        }
        return userResource
                .orElseThrow(() -> new IllegalArgumentException(String.format("User %s not found!", userId)));
    }

    public UserResource updateUser(UserResource userResource) {
        if (userResource.getUserType().equals(UserType.SCRIBE)) {
            Scribe scribe = userResource.asScribe();
            return scribeRepository.update(scribe).asUserResource();
        } else {
            Candidate candidate = userResource.asCandidate();
            return candidateRepository.update(candidate).asUserResource();
        }
    }
}
