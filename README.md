# DevOps Todo App: CI/CD Pipeline, Ansible and Kubernetes

Self Learning Activity for **DevOps (24CYAEC59)**, 5th Semester B.E. CSE (Cyber Security), M. S. Ramaiah Institute of Technology.

- **Student:** Aksha K V (USN: 1MS25CY402)
- **Guide:** Mr. Prashanth Reddy C, Senior Lecturer
- **Repository:** https://github.com/aksha2005/devops-todo-app

One small application is taken through the whole pipeline:

```
Code -> Git/GitHub -> Maven + pytest -> Jenkins -> Docker -> Ansible -> Kubernetes (Minikube) -> Scale
```

## Application

A Todo REST API written with Python Flask, with no database (records are kept in memory).

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/` | API information |
| GET | `/health` | Health check, returns `{"status": "UP"}` |
| GET | `/version` | Returns the application version |
| GET | `/todos` | List all todos |
| POST | `/todos` | Create a todo (title required) |
| PUT | `/todos/<id>` | Update a todo |
| DELETE | `/todos/<id>` | Delete a todo |

A separate Spring Boot Maven Todo API is in `java-todo-app/todo-app`. It is built and tested by Maven (`mvnw clean verify`) in the Jenkins pipeline to demonstrate the Maven build.

## Repository structure

```
app.py, test_app.py, requirements.txt   Flask app and pytest tests
Dockerfile                              Container image for the Flask app
Jenkinsfile                             Jenkins pipeline (Pipeline from SCM)
jenkins-docker/                         Jenkins image with Java, Python and Docker CLI
.github/workflows/ci.yml                GitHub Actions workflow
java-todo-app/todo-app/                 Spring Boot app with pom.xml and Maven Wrapper
ansible/                                Inventory, playbook and the setup_app role
k8s/                                    ConfigMap, Deployment and Service manifests
repo_link.txt                           Repository link for submission
```

## Run locally

```bash
pip install -r requirements.txt
python app.py                 # starts on http://localhost:5000
curl http://localhost:5000/health
pytest -v                     # runs the Flask tests
```

Java app tests:

```bash
cd java-todo-app/todo-app
./mvnw clean verify
```

## Component 1: Git, Maven and Jenkins (CI)

- Work was done on feature branches and merged into `main` through merges and pull requests.
- The Jenkins job uses **Pipeline script from SCM** and reads the `Jenkinsfile` from this repository.
- **Poll SCM** (`H/2 * * * *`) starts a build automatically when `main` changes. Polling is used because Jenkins runs locally in Docker and GitHub cannot send it a webhook.
- Pipeline stages: Checkout, Check Java Version, Build and Test Java Maven App, Install Python Dependencies, Run Python Unit Tests, Build Docker Image, Generate Build Information, Archive Java JAR.
- Post-build actions: publish JUnit results and archive the JAR, `test-results.xml` and `build-info.txt`.
- **CI enhancements:** automated tests (Maven and pytest) in the pipeline, build-number Docker tags (`devops-todo-app:<build number>`), and a GitHub Actions workflow that also tests and builds the image on every push.

## Component 2: Ansible and Kubernetes

**Docker**

```bash
docker build -t devops-todo-app:latest .
docker run -d -p 5000:5000 --name devops-todo-app devops-todo-app:latest
```

The image runs as a non-root user and has a container health check.

**Ansible** (inventory, variables, a handler and the `setup_app` role)

```bash
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

The role checks Docker, verifies the image exists, and starts the container. A handler then calls `/health` and requires HTTP 200. A second run reports `changed=0`, showing the playbook is idempotent.

**Kubernetes on Minikube**

```bash
minikube start
minikube image load devops-todo-app:latest
kubectl apply -f k8s/
kubectl get pods
kubectl get svc
```

- `k8s/deployment.yaml`: 3 replicas, labels and selectors (`app: devops-todo-app`), liveness and readiness probes on `/health`, and CPU and memory requests and limits.
- `k8s/service.yaml`: NodePort Service on port 30080 that forwards to container port 5000.
- `k8s/configmap.yaml`: `PORT`, `APP_ENV` and `LOG_LEVEL`. This is the **deployment enhancement**, together with the health probes.

Scaling demonstration:

```bash
kubectl scale deployment devops-todo-app --replicas=5
kubectl get pods
kubectl scale deployment devops-todo-app --replicas=3
```

Open the app through the Minikube Service URL (see `minikube service list`) and check `/health` and `/todos`.

## Test summary

| Suite | Tests |
|---|---|
| Flask pytest (`test_app.py`) | 10 (9 originally, plus `test_version`) |
| Java Maven (`TodoAppApplicationTests`) | 7 |

## Tools used

Python 3 and Flask, pytest, Java 21 and Spring Boot with Maven, Git and GitHub, Jenkins, GitHub Actions, Docker, Ansible (`community.docker`), Kubernetes with Minikube and kubectl.
