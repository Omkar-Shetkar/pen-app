package com.pen.scribe.service;

import com.pen.client.register.UserResource;
import com.pen.client.scribe.ScribeSearchFilter;
import com.pen.client.scribe.ScribeUpdate;
import com.pen.scribe.domain.Availability;
import com.pen.scribe.domain.Scribe;
import com.pen.scribe.domain.ScribeLanguage;
import com.pen.scribe.repository.ScribeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScribeService {

    private final ScribeRepository scribeRepository;

    ScribeService(ScribeRepository scribeRepository){
        this.scribeRepository = scribeRepository;
    }

    public final Scribe updateScribeAvailability(String scribeId, ScribeUpdate scribeUpdate) {

        Scribe scribe = findById(scribeId);
        scribe.setAvailability(scribeUpdate.getAvailability());
        log.info("updateScribeAvailability  scribeId{}, scribeUpdate{}", scribeId, scribeUpdate);
        return scribeRepository.save(scribe);
    }

    /**
     * @param scribeId
     * @return Scribe
     * @throws IllegalArgumentException - For invalid scribe id
     */
    public Scribe findById(String scribeId) {
        log.info("findScribe by scribeId{}", scribeId );
        return scribeRepository.findById(scribeId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find scribe id: " + scribeId));
    }

    public Set<Scribe> searchScribes(ScribeSearchFilter searchFilter) {
        log.info("Search scribe by filter candidate{} from{} To{} language{}, city{}", searchFilter.getCandidate(), searchFilter.getFrom(),
                searchFilter.getTo(), searchFilter.getLanguage(), searchFilter.getCity());
        return scribeRepository.findByAvailabilityAndLanguage(searchFilter.getCandidate(), searchFilter.getFrom(),
                searchFilter.getTo(), searchFilter.getLanguage(), searchFilter.getCity());
    }

    public Iterable<Scribe> getAll() {
        return scribeRepository.findAll();
    }

    public Scribe create(String userId, UserResource user) {
        log.info("CreateScribe userId{}, user{}", userId, user);
        Set<ScribeLanguage> scribeLanguages = user.getLanguages().stream()
                .map(ScribeLanguage::new)
                .collect(Collectors.toSet());
        Scribe scribe = new Scribe(userId, user.getFirstName(), user.getLastName(), user.getGender(), scribeLanguages, user.getCity(),
                user.getLocation(), Availability.ANY_DAY, user.getMobile(), user.getEmail());

        return scribeRepository.insert(scribe);
    }
}