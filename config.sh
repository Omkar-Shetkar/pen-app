#!/bin/bash

echo Postgres password?

read password

kubectl create secret generic postgres-secret --from-literal=password=$password

echo Keycloak server location? \(eg., http://localhost:8080/auth\)

read server-location

kubectl create cm keycloak-server --from-literal=server=$server-location

echo Keycloak admin-cli secret?

read admin-cli-secret

kubectl create secret generic admin-cli-secret --from-literal=secret=$admin-cli-secret

echo Keycloak backend secret?

read backend-secret

kubectl create secret generic backend-secret --from-literal=secret=$backend-secret

echo Keycloak admin login password?

read keycloak-admin-password

kubectl create secret generic keycloak-admin-password --from-literal=password=$keycloak-admin-password

echo Keycloak db password?

read keycloak-db-password

kubectl create secret generic keycloak-db-password --from-literal=password=$keycloak-db-password

echo Keycloak pen realm JSON relative location?

read realm-location

kubectl create cm keycloak-realm-pen --from-file=pen-realm=$realm-location

echo Backend SPRING_APPLICATION_JSON JSON file location?

read spring-application-json

kubectl create cm spring-application-json --from-file=application-env=$spring-application-json

