# pen-infrastructure

Infrastructure code for Pen app

## Prerequisites

- Create db and users for postgres and keycloak

```
create database light;
create database keycloak;
create user keycloak PASSWORD 'keycloak';

```

- Copy `realm-export.json` to mounted location of keycloak
- Configure client `admin-cli` of `master` realm, as per https://github.com/keycloak/keycloak-documentation/blob/master/server_development/topics/admin-rest-api.adoc#authenticate-with-a-service-account
