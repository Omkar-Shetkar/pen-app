package com.light.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("LANGUAGE")
public class Language {

    @Id
    private long id;

    private String name;

}
