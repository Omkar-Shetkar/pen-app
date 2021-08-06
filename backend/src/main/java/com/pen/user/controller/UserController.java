package com.pen.user.controller;

import com.pen.client.register.UserResource;
import com.pen.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/rest/users")
public class UserController {

    @Autowired
    private UserService userService;

    public static final String SINGLE = "/rest/users/{id}";

    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUser(@PathVariable("id") String userId) {
        UserResource user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResource> updateUser(@PathVariable("id") String userId,
                                                   @RequestBody UserResource userResource) {
        try {
            UserResource updatedUser = userService.updateUser(userResource);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("An error occurred while updating user {}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
