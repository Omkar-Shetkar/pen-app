package com.light.candidate.repository;

import com.light.candidate.domain.Candidate;
import org.springframework.data.repository.CrudRepository;

public interface CandidateRepository extends CrudRepository<Candidate, Long> {
}
