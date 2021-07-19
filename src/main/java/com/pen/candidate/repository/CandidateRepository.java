package com.pen.candidate.repository;

import com.pen.candidate.domain.Candidate;
import org.springframework.data.repository.CrudRepository;

public interface CandidateRepository extends CrudRepository<Candidate, String>, CustomCandidateRepository {
}
