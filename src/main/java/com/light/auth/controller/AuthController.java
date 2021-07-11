package com.light.auth.controller;

import com.light.auth.service.AuthService;
import com.light.client.auth.UserCredentials;
import com.light.client.register.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody UserCredentials credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }

    @PostMapping("/user")
    public ResponseEntity<UserRepresentation> createUser(@RequestBody UserResource userResource) {
        return ResponseEntity.ok(authService.addUser(userResource));
    }
}
