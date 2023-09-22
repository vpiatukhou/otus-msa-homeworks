# README

## Установка

Выполните следующие команды из корневой директории:

```
kubectl create namespace app
helm dependency build ./kubernetes/app-chart
helm install app ./kubernetes/app-chart -f ./kubernetes/app-chart/values.yaml --namespace app
```

Запустить туннель:

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
ПРИМЕЧАНИЕ: в процессе установки приложения создается Persistent Volume объемом 10Mi.
Он не удаляется автоматически, его необходимо удалить вручную командой `kubectl delete pv <имя persistent volume>`.