package com.pen.candidate.repository;

import com.pen.candidate.domain.Candidate;

public interface CustomCandidateRepository {
    public Candidate insert(Candidate candidate);

    public Candidate update(Candidate candidate);
}
