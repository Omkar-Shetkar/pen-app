package com.light.auth.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.stereotype.Component;

@Component
public class KeyCloakConfig {

    private Keycloak keycloak;

    private KeycloakProperties keycloakProperties;

    public KeyCloakConfig(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    private final int POOL_SIZE = 10;

    public Keycloak getKeycloak() {
        if (keycloak == null) {
            // Configuration to connect to client `admin-cli` of realm `master`
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakProperties.getServer())
                    .realm(keycloakProperties.getAdmin().get("realm"))
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(keycloakProperties.getAdmin().get("client-id"))
                    .clientSecret(keycloakProperties.getAdmin().get("client-secret"))
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(POOL_SIZE)
                            .build()
                    )
                    .build();
        }
        return keycloak;
    }
}