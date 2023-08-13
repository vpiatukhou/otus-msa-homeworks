# README

All commands below must be executed in the root directory.

## Install PostgreSQL

`kubectl create namespace postgres`

`helm install postgres bitnami/postgresql --version 12.8.0 -f ./kubernetes/postgres/values.yml --namespace postgres`

## Install the application

`kubectl create namespace app`

`helm install app ./kubernetes/app-chart --namespace app -f ./kubernetes/app-chart/values.yaml`

Start a tunnel:

`minikube tunnel`

## Send requests to the application

Please see examples of request and responses in the Postman collection: 

`postman/hw-03.postman_collection.json`

Requests in the collection depend on each other, so please run the whole collection (not a separate request). 