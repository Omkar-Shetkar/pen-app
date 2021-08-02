# pen-infrastructure

Infrastructure code for Pen app

## Postgres

- Deploy postgres pod and service.

`kubectl apply -f postgres`

- Connect to remote postgres.

`kubectl port-forward postgres-85b84bf857-995jp 6000:5432`

- Create db and users for postgres and keycloak

```
create database pen;
create database keycloak;
create user keycloak PASSWORD 'keycloak';

```

## Keycloak

### Realm configuration (one time)

- Copy `pen-realm-export.json` to volume mount of keycloak container mentioned in the corresponding deployment configuration.

- Login to remote keycloak.

`kubectl port-forward keycloak-78fc9b8c8b-4fs7s 9090:8080`

- Configure client `admin-cli` of `master` realm, as per https://github.com/keycloak/keycloak-documentation/blob/master/server_development/topics/admin-rest-api.adoc#authenticate-with-a-service-account

  - master -> clients -> admin-cli

    - Under `Settings`, enable service accounts.
      - access type: confidential
      - service accounts enabled: ON
    - Under `Mappers`, create a new `audience-mapper`.
      - name: audience-mapper
      - mapper type: audience
      - included custom audience: security-admin-console
    - Under `Service Account Roles`
      - Add `admin` and `create-realm` to `Assigned Roles`.

- Access token will expire in 8 days.

### Deployment

- Deploy keycloak

`kubectl apply -f keycloak`

## Backend

- Build the image

`./mvnw spring-boot:build-image`

- Tag the image

`docker tag pen-backend:0.0.1-SNAPSHOT omkarshetkar/pen-backend:0.0.1`

- Push the image to docker repository

`docker push omkarshetkar/pen-backend:0.0.1`

- `backend-service` will expose external ip to connect to application.
