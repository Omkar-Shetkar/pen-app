package com.pen.user.service;

import com.pen.candidate.domain.Candidate;
import com.pen.candidate.repository.CandidateRepository;
import com.pen.client.exam.UserType;
import com.pen.client.register.UserResource;
import com.pen.scribe.domain.Scribe;
import com.pen.scribe.repository.ScribeRepository;
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
