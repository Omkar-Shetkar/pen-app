package com.pen.auth.service;

import com.pen.auth.config.KeyCloakConfig;
import com.pen.candidate.service.CandidateService;
import com.pen.client.auth.UserCredentials;
import com.pen.client.exam.UserType;
import com.pen.client.register.UserResource;
import com.pen.scribe.service.ScribeService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Slf4j
@Service
public class AuthService {

    private final ScribeService scribeService;
    private final CandidateService candidateService;
    private final KeyCloakConfig keyCloakConfig;

    public AuthService(ScribeService scribeService, CandidateService candidateService,
                       KeyCloakConfig keyCloakConfig) {
        this.scribeService = scribeService;
        this.candidateService = candidateService;
        this.keyCloakConfig = keyCloakConfig;
    }

    public AccessTokenResponse login(UserCredentials credentials) {
        AuthzClient authzClient = AuthzClient.create();
        AccessTokenResponse response = authzClient.obtainAccessToken(credentials.getUsername(),
                credentials.getPassword());
        return response;
    }

    public String addUser(UserResource userResource) {
        Keycloak keycloak = keyCloakConfig.getKeycloak();
        UsersResource usersResource = keycloak.realm("light").users();
        CredentialRepresentation credentialRepresentation =
                createPasswordCredentials(userResource.getCredentials().getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userResource.getCredentials().getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(userResource.getFirstName());
        kcUser.setLastName(userResource.getLastName());
        kcUser.setEmail(userResource.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        Response response = usersResource.create(kcUser);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
            String location = response.getHeaderString("location");
            String userId = location.substring(location.lastIndexOf('/') + 1);
            if (userResource.getUserType().equals(UserType.SCRIBE)) {
                scribeService.create(userId, userResource);
            } else {
                candidateService.create(userId, userResource);
            }
            return userId;
        } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new IllegalArgumentException(String.format("Account already exists for mobile %s",
                    userResource.getMobile()));
        } else {
            throw new RuntimeException(String.format("An error occurred during registration of %s",
                    userResource.getFirstName()));
        }
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
