package com.light.scribe.repository;

import com.light.scribe.domain.Scribe;

public interface CustomScribeRepository {
    public Scribe insert(Scribe scribe);

    public Scribe update(Scribe scribe);
}
