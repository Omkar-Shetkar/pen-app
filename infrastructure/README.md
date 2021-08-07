# pen-infrastructure

Infrastructure code for Pen app

## Postgres

- Create postgres secret (one time or whenever DB password changes)

  `kubectl create secret generic postgres-secret --from-literal=password=<postgres-password>`

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

#### Realm configuration (one time or whenever realm configuration changes)

- Create config map for keycloak server location

`kubectl create cm keycloak-server --from-literal=server=<http://<server-location>/auth>`

Eg.,

`kubectl create cm keycloak-server --from-literal=server=http://keycloak-service:8080/auth`

- Create secrets each for `admin-cli` and `backend` clients.

  `kubectl create secret generic admin-cli-secret --from-literal=secret=<admin-cli-secret>`

  `kubectl create secret generic backend-secret --from-literal=secret=<backend-secret>`

- Create secret for `admin` password.

  `kubectl create secret generic keycloak-admin-password --from-literal=password=<keycloak-admin-password>`

- Create secret for `keycloak` db password.

  `kubectl create secret generic keycloak-db-password --from-literal=password=<keycloak-db-password>`

- Create config map for realm export json.

  `kubectl create cm keycloak-realm-pen --from-file=pen-realm=<file-location>`

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

#### Deployment

- Deploy keycloak

  `kubectl apply -f keycloak`

## Backend

- Create config map for `SPRING_APPLICATION_JSON`

`kubectl create cm spring-application-json --from-file=application-env=./backend/config/spring-application-json.json`

- Build the image

  `./mvnw spring-boot:build-image`

- Tag the image

  `docker tag pen-backend:0.0.1-SNAPSHOT omkarshetkar/pen-backend:0.0.1`

- Push the image to docker repository

  `docker push omkarshetkar/pen-backend:0.0.1`

- Deploy backend

  `kubectl apply -f backend`

- `backend-service` will expose external ip to connect to application.

## Frontend

- Update `pen-frontend/app/config/settings.js` to point to above obtained external ip.

## Deployment to GCP

- Ensure `Container Engine API` and `Kubernetes Engine API` are enabled in cloud console.

- Open cloud shell in GCP console.

- Create cluster for GKE (one time)

  ```
  gcloud container clusters create "pen-gke-regional" \
   --region "us-west1" \
    --machine-type "e2-small" --disk-type "pd-standard" --disk-size "100" \
     --num-nodes "2" --node-locations "us-west1-a,us-west1-b"
  ```

  - Update cloud shell with cluster details

    `gcloud container clusters get-credentials pen-gke-regional --region us-west1`

- git clone https://github.com/Omkar-Shetkar/pen-infrastructure.git

- `cd pen-infrastructure/`

- Create all config maps and secrets using `./scripts/config.sh` (one time)

- Create regionwide pv/pvc for database.

  `kubectl apply -f postgres/prod/storage.yaml`

- Deploy postgres pod and service.

  `kubectl apply -f postgres`

- Create DB and users using

  `./scripts/db-init.sh`. (one time)

- Deploy Keycloak and backend

  `kubectl apply -f keycloak`

  `kubectl apply -f backend`

- Configure Keycloak for audience mapper (one time)
