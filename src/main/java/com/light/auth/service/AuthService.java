package com.light.auth.service;

import com.light.auth.domain.UserCredentials;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public AccessTokenResponse login(UserCredentials credentials) {
        AuthzClient authzClient = AuthzClient.create();
        AccessTokenResponse response = authzClient.obtainAccessToken(credentials.username(),
                credentials.password());
        return response;

    }
}
