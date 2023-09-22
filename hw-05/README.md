# README

## Установка

Выполните следующие команды из корневой директории:

```
kubectl create namespace app
helm dependency build ./kubernetes/app-chart
helm install app ./kubernetes/app-chart -f ./kubernetes/app-chart/values.yaml --namespace app
```

Для запуска приложению требуется около 30 секунд.

Создайте туннель:

`minikube tunnel`

## Отправка запросов

Коллекция postman содержит последовательность запросов, согласно описанию в ДЗ:

`data/hw-05.postman_collection.json`

## Удаление

Выполните следующие команды:
```
helm uninstall app --namespace app
kubectl delete namespace app
```