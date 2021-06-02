package com.light.scribe.service;

import com.light.scribe.client.ScribeUpdate;
import com.light.scribe.domain.Scribe;
import com.light.scribe.repository.ScribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScribeService {

    @Autowired
    private ScribeRepository scribeRepository;

    public final Scribe updateScribeAvailability(long scribeId, ScribeUpdate scribeUpdate) {

        Scribe scribe = findById(scribeId);
        scribe.setAvailabilities(scribeUpdate.getAvailabilities());

        return scribeRepository.save(scribe);
    }

    /**
     * @param scribeId
     * @return
     * @throws IllegalArgumentException - For invalid scribe id
     */
    public Scribe findById(long scribeId) {
        return scribeRepository.findById(scribeId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find scribe id: " + scribeId));
    }

}