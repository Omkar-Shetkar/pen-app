#!/bin/bash

# Create DB and users (one time)

kubectl exec -it $(kubectl get pod --selector="app=postgres" \
  --output jsonpath='{.items[0].metadata.name}') -- bash -c \
  "psql -U postgres -c 'CREATE DATABASE pen'  -c 'CREATE DATABASE keycloak' -c \"CREATE USER keycloak WITH PASSWORD 'keycloak'\" "