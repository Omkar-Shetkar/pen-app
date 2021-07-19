package com.pen.scribe.controller;

import com.pen.client.scribe.ScribeSearchFilter;
import com.pen.client.scribe.ScribeUpdate;
import com.pen.scribe.domain.Scribe;
import com.pen.scribe.service.ScribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/rest/scribes")
public class ScribeController {

    public static final String ALL = "/rest/scribes/";
    public static final String UPDATE = ALL + "{id}/";

    @Autowired
    private ScribeService scribeService;

    //TODO: Creation of Scribe to be handled
    @PostMapping
    public ResponseEntity<Scribe> create() {
        return ResponseEntity.status(CREATED).body(null);
    }

    /**
     * @param id           Scribe id
     * @param scribeUpdate
     * @return
     */
    @PutMapping("{id}/")
    public ResponseEntity<Scribe> updateScribe(@PathVariable String id, @Validated @RequestBody ScribeUpdate scribeUpdate) {
        try {
            Scribe scribe = scribeService.updateScribeAvailability(id, scribeUpdate);
            return ResponseEntity.status(CREATED).body(scribe);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Find by exam schedule, language, and venue location
     *
     * @param searchFilter
     * @return
     */
    @GetMapping
    public ResponseEntity<Set<Scribe>> find(@Validated ScribeSearchFilter searchFilter) {
        Set<Scribe> all = scribeService.searchScribes(searchFilter);
        return ResponseEntity.ok(all);
    }

}