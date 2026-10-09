# Java CI/CD Demo (Jenkins + Docker)

Spring Boot app with a Jenkins pipeline: Git push -> Maven build/test -> Docker image -> Docker Hub -> deploy -> smoke test.

## Run locally
```bash
mvn clean package
java -jar target/java-cicd-demo-1.0.0.jar
curl localhost:8080
```

## Run with Docker
```bash
docker build -t java-cicd-demo .
docker run -d -p 8080:8080 java-cicd-demo
```

## Before using the Jenkinsfile
1. Replace `YOUR_DOCKERHUB_USER` and `YOUR_GITHUB_USER`.
2. In Jenkins, configure tools `jdk17` and `maven3`.
3. Add credentials with ID `dockerhub-creds` (Docker Hub username + access token).
4. Add user `jenkins` to the `docker` group and restart Jenkins.
5. Create a Pipeline job (Pipeline script from SCM) and add a GitHub webhook to `http://<jenkins-ip>:8080/github-webhook/`.

## Endpoints
- `/` hello message
- `/actuator/health` health check
