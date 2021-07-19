package com.pen.client.scribe;

import com.pen.scribe.domain.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScribeUpdate {

    private Availability availability;
}
