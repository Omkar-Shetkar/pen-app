package com.pen.auth.controller;

import com.pen.auth.service.AuthService;
import com.pen.client.auth.UserCredentials;
import com.pen.client.register.UserResource;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rest/auth")
@Slf4j
public class AuthController {

    final private AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody UserCredentials credentials) {
        try {
            return ResponseEntity.ok(authService.login(credentials));
        } catch (HttpResponseException exception) {
            throw new ResponseStatusException(HttpStatus.valueOf(exception.getStatusCode()), exception.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserResource userResource) {
        try {
            return ResponseEntity.ok(authService.addUser(userResource));
        } catch (HttpResponseException exception) {
            throw new ResponseStatusException(HttpStatus.valueOf(exception.getStatusCode()), exception.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred during user registration", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
