# pen-infrastructure

Infrastructure code for Pen app

## Prerequisites

- Create db and users for postgres and keycloak

```
create database pen;
create database keycloak;
create user keycloak PASSWORD 'keycloak';

```

- Copy `pen-realm-export.json` to volume mount of keycloak container mentioned in the corresponding deployment configuration.

- Configure client `admin-cli` of `master` realm, as per https://github.com/keycloak/keycloak-documentation/blob/master/server_development/topics/admin-rest-api.adoc#authenticate-with-a-service-account

- Access token will expire in 8 days.
