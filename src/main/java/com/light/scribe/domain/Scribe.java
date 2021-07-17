package com.light.scribe.domain;

import com.light.client.exam.UserType;
import com.light.client.register.UserResource;
import com.light.domain.Gender;
import com.light.domain.Language;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents person who will be writing exam for a candidate.
 */
@Data
public class Scribe {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Set<ScribeLanguage> languages = new HashSet<>();

    private Long city;

    private String location;

    private Availability availability = Availability.ANY_DAY;

    private String mobile;

    private String email;

    public Scribe(String id, String firstName, String lastName, Gender gender, Long city,
                  String location, String mobile, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.city = city;
        this.location = location;
        this.mobile = mobile;
        this.email = email;
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

    public UserResource asUserResource() {
        Set<Long> languageIds = languages.stream().map(ScribeLanguage::getLanguage).collect(Collectors.toSet());
        return new UserResource(UserType.SCRIBE, firstName, lastName, gender, mobile, email, city,
                location, languageIds, availability, null);
    }

}
