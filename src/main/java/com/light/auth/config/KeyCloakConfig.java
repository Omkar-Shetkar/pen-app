package com.light.auth.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeyCloakConfig {

    static Keycloak keycloak = null;
    final static String serverUrl = "http://localhost:8080/auth";
    final static String realm = "master";
    final static String clientId = "admin-cli";
    final static String clientSecret = "f399089c-1aea-4673-b93b-05a6e3b21fbd";

    public KeyCloakConfig() {
    }

    public static Keycloak getInstance() {
        if (keycloak == null) {

//            Configuration to connect to client `admin-cli` of realm `master`
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build()
                    )
                    .build();
        }
        return keycloak;
    }
}