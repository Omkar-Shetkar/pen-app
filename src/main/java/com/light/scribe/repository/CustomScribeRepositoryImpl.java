package com.light.scribe.repository;

import com.light.scribe.domain.Scribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

public class CustomScribeRepositoryImpl implements CustomScribeRepository {

    @Autowired
    private JdbcAggregateTemplate template;

    @Override
    public Scribe insert(Scribe scribe) {
        return template.insert(scribe);
    }

    @Override
    public Scribe update(Scribe scribe) {
        return template.update(scribe);
    }
}
