# README

## Установка

Выполните следующие команды из корневой директории:

```
kubectl create namespace app
helm dependency build ./kubernetes/app-chart
helm install app -n app ./kubernetes/app-chart -f ./kubernetes/app-chart/values.yaml
```
Проверьте состояние подов: 

`kubectl -n app get pods`

Возможно, некоторые поды будут в стостоянии **Error** или **CrashLoopBackOff**.
Это **нормально** и вызвано тем что СУБД еще не запустилась. Подождите некоторое время  

Создайте туннель:

`minikube tunnel`

## Отправка запросов

Коллекция postman содержит последовательность запросов, согласно описанию в ДЗ:

`data/hw-06.postman_collection.json`

## Удаление

Выполните следующие команды:
```
helm uninstall app --namespace app
kubectl delete namespace app
```
В процессе установки приложения создается Persistent Volume размером 10Mi.
Возможно его потребуется удалить вручную командой `kubectl delete pv имя_persistent_volume`.