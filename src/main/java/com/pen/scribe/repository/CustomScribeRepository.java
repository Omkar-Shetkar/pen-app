package com.pen.scribe.repository;

import com.pen.scribe.domain.Scribe;

public interface CustomScribeRepository {
     Scribe insert(Scribe scribe);

     Scribe update(Scribe scribe);
}
