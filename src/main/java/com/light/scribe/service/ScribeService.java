package com.light.scribe.service;

import com.light.client.register.UserResource;
import com.light.client.scribe.ScribeSearchFilter;
import com.light.client.scribe.ScribeUpdate;
import com.light.scribe.domain.Scribe;
import com.light.scribe.repository.ScribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ScribeService {

    @Autowired
    private ScribeRepository scribeRepository;

    public final Scribe updateScribeAvailability(String scribeId, ScribeUpdate scribeUpdate) {

        Scribe scribe = findById(scribeId);
        scribe.setAvailabilities(scribeUpdate.getAvailabilities());

        return scribeRepository.save(scribe);
    }

    /**
     * @param scribeId
     * @return
     * @throws IllegalArgumentException - For invalid scribe id
     */
    public Scribe findById(String scribeId) {
        return scribeRepository.findById(scribeId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find scribe id: " + scribeId));
    }

    public Set<Scribe> searchScribes(ScribeSearchFilter searchFilter) {
        return scribeRepository.findByAvailabilityAndLanguage(searchFilter.getFrom(),
                searchFilter.getTo(), searchFilter.getLanguage());
    }

    public Iterable<Scribe> getAll() {
        return scribeRepository.findAll();
    }

    public void create(String userId, UserResource user) {
        Scribe scribe = new Scribe(userId, user.getFirstName(), user.getLastName(), user.getGender(),
                user.getLocation(), user.getMobile(), user.getEmail());
        scribeRepository.insert(scribe);
    }
}