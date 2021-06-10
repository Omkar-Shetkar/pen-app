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

    public static final long UNASSIGNED_ID = 0;

    private long candidate = UNASSIGNED_ID;

    private long scribe = UNASSIGNED_ID;

    private long schedule;

    private RequestStatus requestStatus;


}
