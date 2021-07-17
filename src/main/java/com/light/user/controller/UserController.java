package com.light.user.controller;

import com.light.client.register.UserResource;
import com.light.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        UserResource updatedUser = userService.updateUser(userResource);
        return ResponseEntity.ok(updatedUser);
    }
}
