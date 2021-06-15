package com.light.request.client;

import com.light.request.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestSearchFilter {

    private Long candidate;

    private Long scribe;

    private Long schedule;

    private RequestStatus requestStatus;


}
