package com.light.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("EXAM")
public class Exam {

    @Id
    private Long id;

    private String name;

    public Exam(String name) {
        this.name = name;
    }

}
