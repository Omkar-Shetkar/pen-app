package com.light.scribe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("SCRIBE_LANGUAGE")
@AllArgsConstructor
public class LanguageRef {
    Long language;
}
