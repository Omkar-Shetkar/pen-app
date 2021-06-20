package com.light.scribe.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import java.util.Date;

/**
 * Filters to search scribe for scheduled exam based language, location etc.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScribeSearchFilter {

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date from;

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date to;

    @NonNull
    private Long language;

    private String location;

    public ScribeSearchFilter(Date from, Date to, Long language) {
        this.from = from;
        this.to = to;
        this.language = language;
    }

}
