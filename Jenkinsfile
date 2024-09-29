pipeline {
    agent any
    
    environment {
        DOCKER_HUB_CREDENTIALS = 'dockerhub' // Substitua pelo ID das credenciais do Docker Hub no Jenkins
        DOCKER_HUB_NAMESPACE = 'strobson' // Namespace no Docker Hub
        DOCKER_HUB_PASSWORD = 'docker-pwd'
        DOCKER_HUB_USERNAME = 'st.robson.costa@gmail.com'        
        K8S_CONTEXT = 'minikube' // Contexto do Kubernetes (minikube no caso)
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
		    sh """
		    echo "${DOCKER_HUB_PASSWORD}" | docker login -u "${DOCKER_HUB_USERNAME}" --password-stdin
		    """
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

        stage('Deploy to Kubernetes') {
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
            steps {
                // Verifica o status dos pods no Minikube
                sh 'kubectl get pods -o wide'
                // Verifica o status dos serviços
                sh 'kubectl get services'
            }
        }
        
        stage('Minikube Tunnel') {
	    steps {
		// Inicia o túnel do Minikube
		sh 'minikube tunnel --bind-address=192.168.0.106 &'
	    }
	}
    }

    post {
        always {
            script {
                // Coleta os logs dos pods como pós-execução
                sh 'kubectl logs -l app=server'
                sh 'kubectl logs -l app=gateway'
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

