pipeline {
    agent any

    tools {
        jdk 'jdk21'
        maven 'maven3'
    }

    environment {
        DOCKER_IMAGE = "apurva2318/java-cicd-demo"
        IMAGE_TAG    = "${BUILD_NUMBER}"
        DOCKER_CREDS = credentials('dockerhub-creds')
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Apurva-Solanke/java-cicd-demo.git'
            }
        }

        stage('Build & Unit Test') {
            steps {
                sh 'mvn clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} .
                    docker tag ${DOCKER_IMAGE}:${IMAGE_TAG} ${DOCKER_IMAGE}:latest
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh """
                    echo "${DOCKER_CREDS_PSW}" | docker login -u "${DOCKER_CREDS_USR}" --password-stdin
                    docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
                    docker push ${DOCKER_IMAGE}:latest
                """
            }
        }

        stage('Deploy') {
            steps {
                sh """
                    docker pull ${DOCKER_IMAGE}:${IMAGE_TAG}
                    docker stop java-app || true
                    docker rm java-app || true
                    docker run -d --name java-app \
                        --restart unless-stopped \
                        -p 9090:8080 \
                        ${DOCKER_IMAGE}:${IMAGE_TAG}
                """
            }
        }

        stage('Smoke Test') {
            steps {
                sh '''
                    sleep 15
                    curl -f http://localhost:9090/actuator/health
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
            sh 'docker image prune -f || true'
        }
        success {
            echo "Deployed build ${BUILD_NUMBER} successfully"
        }
        failure {
            echo "Pipeline failed, check the logs"
        }
    }
}
