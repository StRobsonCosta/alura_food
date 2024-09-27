### PASSO A PASSO


# No Diretório da Aplicação:

O diretório deve conter os Microsserviços (server, gateway,pedidos, etc)

## Terminal dentro do diretório:
### Comandos:

- Inicie o Minikube:
minikube start

- Faça uma imagem no docker de cada ms:
docker build -t strobson/java-server-k8s:v3 server/.
docker build -t strobson/java-gateway-k8s:v3 gateway/.
docker build -t strobson/java-pagamentos-k8s:v3 pagamentos/.
docker build -t strobson/java-pedidos-k8s:v3 pedidos/.
docker build -t strobson/java-avaliacao-k8s:v3 avaliacao/.

- Suba a imagem para o dockerhub:
docker push strobson/java-server-k8s:v3
docker push strobson/java-gateway-k8s:v3
docker push strobson/java-pagamentos-k8s:v3
docker push strobson/java-pedidos-k8s:v3
docker push strobson/java-avaliacao-k8s:v3

- Atualize os pods no Kubernetes:
kubectl apply -f k8s/app.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/loadbalancer.yaml
kubectl apply -f k8s/mysql.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/services.yaml
kubectl apply -f k8s/volumes.yaml
ou (kubectl apply -f k8s/app.yaml -f k8s/configmap.yaml -f k8s/loadbalancer.yaml -f k8s/mysql.yaml -f k8s/secrets.yaml -f k8s/services.yaml -f k8s/volumes.yaml)

- Ative os addons:
minukube addons list
minikube addons enable csi-hostpath-driver
(valide se dashboard, default-storageclass tb estão ativos)

- Crie ou Inicie o Tunnel para o servico de gateway e loadbalancer:
minikube tunnel --bind-address=192.168.0.106
ou minikube status

- Inicie o Dashboard:
minikube dashboard

Analise o Eureka:
http://192.168.0.106:8081/


