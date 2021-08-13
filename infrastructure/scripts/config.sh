#!/bin/bash

# Recreates all config and secrets

echo Delete all config/secret? \(Y/N\)

read delete

if [[ $delete == 'Y' || $delete == 'y' ]]
then
    echo 'Going to delete all config/secret';
    kubectl delete secret admin-cli-secret backend-secret keycloak-admin-password keycloak-db-password postgres-secret postgres-server
    kubectl delete cm keycloak-realm-pen keycloak-server spring-application-json
else
    exit 0;
fi

source ~/pen-app/infrastructure/scripts/secret-values.txt

echo Read secrets from secret-values.txt. Do you want to override the values? \(Y/N\)

read override

if [[ $override == 'Y' || $override == 'y' ]]
then

    echo Postgres password?

    read password

    echo Postgres server?

    read postgres_server

    echo Keycloak server location? \(eg., http://localhost:8080/auth\)

    read server_location


    echo Keycloak admin-cli secret?

    read admin_cli_secret


    echo Keycloak backend secret?

    read backend_secret


    echo Keycloak admin login password?

    read keycloak_admin_password


    echo Keycloak db password?

    read keycloak_db_password

    echo Keycloak pen realm JSON relative location?

    read realm_location

    echo Backend SPRING_APPLICATION_JSON JSON file location?

    read spring_application_json

fi

kubectl create secret generic postgres-secret --from-literal=password=$password

kubectl create secret generic postgres-server --from-literal=server=$postgres_server

kubectl create cm keycloak-server --from-literal=server=$server_location

kubectl create secret generic admin-cli-secret --from-literal=secret=$admin_cli_secret

kubectl create secret generic backend-secret --from-literal=secret=$backend_secret

kubectl create secret generic keycloak-admin-password --from-literal=password=$keycloak_admin_password

kubectl create secret generic keycloak-db-password --from-literal=password=$keycloak_db_password

kubectl create cm keycloak-realm-pen --from-file=pen-realm=$realm_location

kubectl create cm spring-application-json --from-file=application-env=$spring_application_json
