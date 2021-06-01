package com.light.scribe.domain;

import com.light.domain.Gender;
import com.light.domain.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents person who will be writing exam for a candidate.
 */
@Data
@AllArgsConstructor
public class Scribe {

    @Id
    private long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Set<LanguageRef> languages = new HashSet<>();

    private String location;

    private Set<Availability> availabilities = new HashSet<>();

    private String phone;

    private String email;

    public Scribe(String firstName, String lastName, Gender gender, String location, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.location = location;
        this.phone = phone;
        this.email = email;
    }

    public void addLanguage(Language language) {
        languages.add(createLanguageRef(language));
    }

    private LanguageRef createLanguageRef(Language language) {
        Assert.notNull(language, "Language must not be null");
        Assert.notNull(language.getId(), "Language id, must not be null");

        LanguageRef languageRef = new LanguageRef();
        languageRef.language = language.getId();
        return languageRef;
    }

}

@Table("scribe_languages")
class LanguageRef {
    Long language;
}