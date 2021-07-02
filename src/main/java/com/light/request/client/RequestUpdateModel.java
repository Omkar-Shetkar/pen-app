package com.light.request.client;


import com.light.request.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateModel {

    @NonNull
    private RequestStatus requestStatus;
}
