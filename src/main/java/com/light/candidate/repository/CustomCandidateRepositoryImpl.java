package com.light.candidate.repository;

import com.light.candidate.domain.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

public class CustomCandidateRepositoryImpl implements CustomCandidateRepository {

    @Autowired
    private JdbcAggregateTemplate template;

    @Override
    public Candidate insert(Candidate candidate) {
        return template.insert(candidate);
    }

    @Override
    public Candidate update(Candidate candidate) {
        return template.update(candidate);
    }
}
