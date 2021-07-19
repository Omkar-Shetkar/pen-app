package com.pen.scribe.repository;

import com.pen.scribe.domain.Scribe;

public interface CustomScribeRepository {
    public Scribe insert(Scribe scribe);

    public Scribe update(Scribe scribe);
}
