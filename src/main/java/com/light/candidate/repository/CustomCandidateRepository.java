package com.light.candidate.repository;

import com.light.candidate.domain.Candidate;

public interface CustomCandidateRepository {
    public Candidate insert(Candidate candidate);

    public Candidate update(Candidate candidate);
}
