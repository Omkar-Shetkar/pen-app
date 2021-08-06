package com.pen.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Language {

    @Id
    private Long id;

    @NonNull
    private String name;

}
