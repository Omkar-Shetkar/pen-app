package com.light.client.scribe;

import com.light.scribe.domain.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScribeUpdate {

    private Availability availability;
}
