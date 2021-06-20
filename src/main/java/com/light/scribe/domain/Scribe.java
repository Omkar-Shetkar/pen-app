package com.light.scribe.domain;

import com.light.domain.Gender;
import com.light.domain.Language;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents person who will be writing exam for a candidate.
 */
@Data
public class Scribe {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Set<ScribeLanguage> languages = new HashSet<>();

    private String location;

    private Set<Availability> availabilities = new HashSet<>();

    private String phone;

    private String email;

    public Scribe(String firstName, String lastName, Gender gender, String location, String phone, String email) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.location = location;
        this.phone = phone;
        this.email = email;
    }

    public Set<Availability> getAvailabilities() {
        return Collections.unmodifiableSet(availabilities);
    }

    public Set<ScribeLanguage> getLanguages() {
        return Collections.unmodifiableSet(languages);
    }

    public void addLanguage(Language language) {
        languages.add(createLanguageRef(language));
    }

    private ScribeLanguage createLanguageRef(Language language) {
        Assert.notNull(language, "Language must not be null");
        Assert.notNull(language.getId(), "Language id, must not be null");

        return new ScribeLanguage(language.getId());
    }

}
