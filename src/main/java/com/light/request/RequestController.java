package com.light.request;

import com.light.request.client.RequestUpdate;
import com.light.request.domain.Request;
import com.light.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rest/requests")
public class RequestController {

    public static final String ALL = "/rest/requests";

    public static final String SINGLE = "/rest/requests/{id}";

    @Autowired
    private RequestService requestService;

    @PostMapping
    public ResponseEntity<Request> create(@Validated @RequestBody RequestUpdate requestUpdate) {

        try {
            Request request = requestService.create(requestUpdate);
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}