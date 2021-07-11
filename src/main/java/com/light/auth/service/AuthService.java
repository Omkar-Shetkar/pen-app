package com.light.auth.service;

import com.light.auth.config.KeyCloakConfig;
import com.light.candidate.service.CandidateService;
import com.light.client.auth.UserCredentials;
import com.light.client.exam.UserType;
import com.light.client.register.UserResource;
import com.light.scribe.service.ScribeService;
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
        AccessTokenResponse response = authzClient.obtainAccessToken(credentials.username(),
                credentials.password());
        return response;
    }

    public UserRepresentation addUser(UserResource userResource) {
        Keycloak keycloak = keyCloakConfig.getKeycloak();
        UsersResource usersResource = keycloak.realm("light").users();
        CredentialRepresentation credentialRepresentation =
                createPasswordCredentials(userResource.getCredentials().password());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userResource.getCredentials().username());
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
        }
        return kcUser;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
