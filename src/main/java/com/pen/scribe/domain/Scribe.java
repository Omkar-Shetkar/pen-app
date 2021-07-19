package com.pen.scribe.domain;

import com.pen.client.exam.UserType;
import com.pen.client.register.UserResource;
import com.pen.domain.Gender;
import com.pen.domain.Language;
import lombok.Builder;
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
@Builder
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

    public Scribe(String id, String firstName, String lastName, Gender gender, Set<ScribeLanguage> languages, Long city,
                  String location, Availability availability, String mobile, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.languages = languages;
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

        return UserResource.builder()
                .userType(UserType.SCRIBE)
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .mobile(mobile)
                .email(email)
                .city(city)
                .location(location)
                .languages(languageIds)
                .availability(availability)
                .build();
    }

}
