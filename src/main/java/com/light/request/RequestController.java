package com.light.request;

import com.light.request.client.RequestCreationModel;
import com.light.request.client.RequestResource;
import com.light.request.client.RequestSearchFilter;
import com.light.request.client.RequestUpdateModel;
import com.light.request.domain.Request;
import com.light.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/rest/requests")
public class RequestController {

    public static final String ALL = "/rest/requests";

    public static final String SINGLE = "/rest/requests/{id}";

    @Autowired
    private RequestService requestService;

    @PostMapping
    public ResponseEntity<Request> create(@Validated @RequestBody RequestCreationModel requestCreationModel) {

        try {
            var request = requestService.create(requestCreationModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Delete a request, given its id.
     *
     * @param id - The request id
     * @return HttpStatus.noContent - If successfully deleted the request
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        try {
            requestService.deleteRequest(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Updates request status.
     *
     * @param id            - The request id
     * @param requestUpdateModel - Request update model
     * @return - Updated request
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Request> update(@PathVariable("id") long id,
                                          @Validated @RequestBody RequestUpdateModel requestUpdateModel) {

        try {
            var request = requestService.update(id, requestUpdateModel.getRequestStatus());
            return ResponseEntity.status(HttpStatus.OK).body(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<RequestResource>> find(@Validated RequestSearchFilter searchFilter) {
        List<RequestResource> requests = requestService.searchRequestResources(searchFilter);
        return ResponseEntity.ok(requests);
    }

}