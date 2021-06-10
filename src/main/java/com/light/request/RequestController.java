package com.light.request;

import com.light.request.client.RequestUpdate;
import com.light.request.domain.Request;
import com.light.request.domain.RequestStatus;
import com.light.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
            var request = requestService.create(requestUpdate);
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Updates request status.
     *
     * @param id
     * @param requestStatus
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Request> update(@PathVariable("id") long id, @Validated @RequestBody RequestStatus requestStatus) {

        try {
            var request = requestService.update(id, requestStatus);
            return ResponseEntity.status(HttpStatus.OK).body(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

}