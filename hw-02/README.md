The steps to run the application in minikube:

Enable Ingress controller addon if it hasn't been done yet:

`minikube addons enable ingress`

Start a tunnel:

`minikube tunnel`

Test the application. The sort URL:

`curl --resolve "arch.homework:80:127.0.0.1" -i http://arch.homework/health`

The long URL:

`curl --resolve "arch.homework:80:127.0.0.1" -i http://arch.homework/otusapp/vpiatukhou/health`

The both requests must return:

`{"status":"OK"}`