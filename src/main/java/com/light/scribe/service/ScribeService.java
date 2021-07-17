package com.light.scribe.service;

import com.light.client.register.UserResource;
import com.light.client.scribe.ScribeSearchFilter;
import com.light.client.scribe.ScribeUpdate;
import com.light.scribe.domain.Availability;
import com.light.scribe.domain.Scribe;
import com.light.scribe.domain.ScribeLanguage;
import com.light.scribe.repository.ScribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScribeService {

    @Autowired
    private ScribeRepository scribeRepository;

    public final Scribe updateScribeAvailability(String scribeId, ScribeUpdate scribeUpdate) {

        Scribe scribe = findById(scribeId);
        scribe.setAvailability(scribeUpdate.getAvailability());

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

    public Scribe create(String userId, UserResource user) {
        Set<ScribeLanguage> scribeLanguages = user.getLanguages().stream()
                .map(l -> new ScribeLanguage(l))
                .collect(Collectors.toSet());
        Scribe scribe = new Scribe(userId, user.getFirstName(), user.getLastName(), user.getGender(), scribeLanguages, user.getCity(),
                user.getLocation(), Availability.ANY_DAY, user.getMobile(), user.getEmail());

        return scribeRepository.insert(scribe);
    }
}