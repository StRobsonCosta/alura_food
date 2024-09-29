pipeline {
    agent any
    
    environment {
        DOCKER_HUB_CREDENTIALS = 'docker_credential' // Substitua pelo ID das credenciais do Docker Hub no Jenkins
        DOCKER_HUB_NAMESPACE = 'strobson' // Namespace no Docker Hub              
        K8S_CONTEXT = 'minikube' // Contexto do Kubernetes (minikube no caso)
        SLACK_CHANNEL = '#pipeline-alurafood' // Canal do Slack
        BRANCH_NAME = "${env.GIT_BRANCH ?: 'unknown'}" // Definindo um valor padrão para BRANCH_NAME
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                // Clona o código fonte da branch
                git branch: 'master', 
                    url: 'git@github.com:StRobsonCosta/alura_food.git',
                    credentialsId: 'github-ssh'
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def services = ['server', 'gateway', 'pagamentos', 'pedidos', 'avaliacao']
                    services.each { service ->
                        // Compila cada microserviço
                        sh "docker build -t ${DOCKER_HUB_NAMESPACE}/java-${service}-k8s:v1-J ${service}/."
                    }
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    // Usar a credencial do Jenkins
                    withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIALS, usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                        // Realiza o login no Docker Hub
                        sh """
                        echo "${DOCKER_HUB_PASSWORD}" | docker login -u "${DOCKER_HUB_USERNAME}" --password-stdin
                        """
                    }
                }
            }
        }

        stage('Push Docker Images to DockerHub') {
            steps {
                script {
                    def services = ['server', 'gateway', 'pagamentos', 'pedidos', 'avaliacao']
                    services.each { service ->
                        // Push das imagens para o Docker Hub
                        sh "docker push ${DOCKER_HUB_NAMESPACE}/java-${service}-k8s:v1-J"
                    }
                }
            }
        }

        stage('Deploy to Docker') {
            when {
                branch 'master' // Executa essa etapa apenas se a branch for 'master'
            }
            steps {
                script {
                    // Aqui você pode rodar os containers no Docker
                    def services = ['server', 'gateway', 'pagamentos', 'pedidos', 'avaliacao']
                    services.each { service ->
                        // Executa o deploy de cada serviço
                        sh "docker run -d -p 8082:8082 ${DOCKER_HUB_NAMESPACE}/java-${service}-k8s:v1-J"
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                branch 'kubernetes' // Executa essa etapa apenas se a branch for 'kubernetes'
            }
            steps {
                script {
                    // Aplica os YAMLs de configuração no Kubernetes (Minikube)
                    sh '''
                    kubectl apply -f k8s/app.yaml
                    kubectl apply -f k8s/configmap.yaml
                    kubectl apply -f k8s/loadbalancer.yaml
                    kubectl apply -f k8s/mysql.yaml
                    kubectl apply -f k8s/secrets.yaml
                    kubectl apply -f k8s/services.yaml
                    kubectl apply -f k8s/volumes.yaml
                    '''
                }
            }
        }

        stage('Verify Deployment') {
            when {
                anyOf {
                    branch 'kubernetes' // Executa essa etapa apenas se a branch for 'kubernetes'
                    branch 'master' // Também executa na branch 'master'
                }
            }
            steps {
                script {
                    if (env.BRANCH_NAME == 'kubernetes') {
                        // Verifica o status dos pods no Minikube
                        sh 'kubectl get pods -o wide'
                        // Verifica o status dos serviços
                        sh 'kubectl get services'
                    } else {
                        // Caso seja a branch master, pode fazer uma verificação se necessário
                        echo "Deployment to Docker completed for branch master."
                    }
                }
            }
        }
        
        stage('Minikube Tunnel') {
            when {
                branch 'kubernetes' // Executa essa etapa apenas se a branch for 'kubernetes'
            }
            steps {
                // Inicia o túnel do Minikube
                sh 'minikube tunnel --bind-address=192.168.0.106 &'
            }
        }

        stage('Notify Slack') {
            steps {
                script {
                    def status = currentBuild.result ?: 'SUCCESS'
                    def message = "Pipeline Status: ${status} in branch: ${BRANCH_NAME}"

                    // Se a branch for kubernetes, adicione o link do dashboard
                    if (BRANCH_NAME == 'kubernetes') {
                        message += "\nMinikube Dashboard: http://192.168.99.100:30000/"
                    }

                    // Se a branch for master, adicione o link do pedido
                    if (BRANCH_NAME == 'master') {
                        message += "\nPedidos Service: http://localhost:8082/pedidos-ms/pedidos"
                    }

                    // Envia a mensagem ao Slack
                    slackSend(channel: SLACK_CHANNEL, message: message)
                }
            }
        }
    }

    post {
        always {
            script {
                // Verifica se estamos na branch 'kubernetes'
                if (BRANCH_NAME == 'kubernetes') {
                    // Exibe o estado dos pods apenas na branch 'kubernetes'
                    sh 'kubectl get pods -o wide'

                    try {
                        // Coleta os logs apenas na branch 'kubernetes'
                        sh 'kubectl logs -l app=server || true'
                        sh 'kubectl logs -l app=gateway || true'
                    } catch (Exception e) {
                        echo "Failed to collect logs: ${e.message}"
                    }
                } else {
                    // Executa uma ação alternativa se estiver na branch 'master'
                    echo "Not on 'kubernetes' branch, skipping log collection."
                }
            }
        }

        success {
            echo 'Deploy completed successfully.'
        }

        failure {
            echo 'Deploy failed. Check the logs.'
        }
    }
}

