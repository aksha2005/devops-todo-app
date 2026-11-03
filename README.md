# DevOps Todo Application

A simple Todo REST API built using Python Flask and deployed using Docker, Ansible, and Kubernetes. This project demonstrates DevOps practices including version control, automated testing, Continuous Integration (CI), containerization, configuration management, and container orchestration.

## Project Overview

The DevOps Todo Application provides REST API endpoints to create, retrieve, update, and delete Todo tasks.

The project demonstrates an automated software delivery workflow using Git, Jenkins, Docker, Ansible, and Kubernetes.

## Technologies Used

| Technology | Purpose |
|---|---|
| Python | Backend programming |
| Flask | REST API development |
| Pytest | Automated testing |
| Git and GitHub | Version control |
| Jenkins | Continuous Integration |
| Docker | Containerization |
| Ansible | Configuration management and automation |
| Kubernetes | Container orchestration |
| Minikube | Local Kubernetes cluster |

## Application Features

- Create new Todo tasks.
- Retrieve all Todo tasks.
- Update existing Todo tasks.
- Delete Todo tasks.
- Health-check endpoint.
- Automated unit testing.

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | Application home |
| GET | `/health` | Application health check |
| GET | `/todos` | Retrieve all Todo tasks |
| POST | `/todos` | Create a Todo task |
| PUT | `/todos/<id>` | Update a Todo task |
| DELETE | `/todos/<id>` | Delete a Todo task |

## Run the Application Locally

### 1. Clone the repository

```bash
git clone https://github.com/aksha2005/devops-todo-app.git
cd devops-todo-app
```

### 2. Create a virtual environment

```bash
python -m venv venv
```

Activate it:

**Windows:**
```bash
venv\Scripts\activate
```

**Linux/macOS:**
```bash
source venv/bin/activate
```

### 3. Install dependencies

```bash
pip install -r requirements.txt
```

### 4. Run the application

```bash
python app.py
```

Open `http://localhost:5000` in your browser.

## Automated Testing

Run the unit tests using:

```bash
pytest -v
```

The project includes nine automated tests covering application routes, health checks, and Todo CRUD operations.

## Jenkins Continuous Integration

Jenkins is configured to retrieve the source code from GitHub and execute the CI pipeline.

Pipeline stages:

1. Checkout source code from GitHub.
2. Create a Python virtual environment.
3. Install project dependencies.
4. Execute automated unit tests.
5. Generate build information.
6. Publish JUnit test results.
7. Archive build artifacts.

The Jenkins pipeline uses Poll SCM to check the GitHub repository for changes.

Archived artifacts:

- `test-results.xml`
- `build-info.txt`

## Docker

Build the Docker image:

```bash
docker build -t devops-todo-app:latest .
```

Run the container:

```bash
docker run -d -p 5000:5000 --name devops-todo-app devops-todo-app:latest
```

Check the running container:

```bash
docker ps
```

## Ansible Automation

Ansible is used to automate application container deployment.

The Ansible implementation includes:

- Inventory configuration.
- Deployment playbook.
- Reusable application role.
- Variables for application configuration.
- Handler to restart the application container.
- Idempotency verification.

Run the playbook:

```bash
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

## Kubernetes Deployment

The application is deployed on a local Kubernetes cluster using Minikube.

Kubernetes resources include:

- Deployment with three replicas.
- NodePort Service.
- ConfigMap for application configuration.
- Liveness probe.
- Readiness probe.
- CPU and memory resource requests and limits.

Apply Kubernetes configurations:

```bash
kubectl apply -f k8s/
```

Check deployments and pods:

```bash
kubectl get deployments
kubectl get pods
kubectl get services
```

Scale the deployment to five replicas:

```bash
kubectl scale deployment devops-todo-app --replicas=5
```

Scale it back to three replicas:

```bash
kubectl scale deployment devops-todo-app --replicas=3
```

## Project Objective

The objective is to demonstrate how a simple application can be developed, tested, containerized, configured, and deployed using common DevOps tools and practices.

## Repository

GitHub: https://github.com/aksha2005/devops-todo-app

## Author

Aksha K V

Computer Science and Engineering (Cyber Security)
