package com.pen.candidate.repository;

import com.pen.candidate.domain.Candidate;

public interface CustomCandidateRepository {
     Candidate insert(Candidate candidate);

     Candidate update(Candidate candidate);
}
