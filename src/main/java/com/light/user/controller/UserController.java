package com.light.user.controller;

import com.light.client.register.UserResource;
import com.light.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
