package com.pen.client.request;

import com.pen.request.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestSearchFilter {

    private String candidate;

    private String scribe;

    private Long schedule;

    private RequestStatus requestStatus;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startingFrom;

}
