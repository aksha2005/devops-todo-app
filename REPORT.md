# DevOps Pipeline & Kubernetes Deployment Report

**Course Code**: 25CIAEC59 / 25CYAEC59  
**Course Title**: DevOps  
**Credits**: 1:0:0 (Contact Hours: 15L)  
**Total Marks**: 20 (Component 1: 10 Marks | Component 2: 10 Marks)  

---

## Student Details
- **Student Name**: aksha2005  
- **Email**: akshakv2005@gmail.com  
- **GitHub Repository**: [https://github.com/aksha2005/devops-todo-app.git](https://github.com/aksha2005/devops-todo-app.git)  
- **Date**: September 28, 2026  

---

## Certificate & Declaration
This is to certify that the project work titled **"DevOps Todo Application: End-to-End CI/CD Pipeline, Ansible Server Configuration, and Kubernetes Scaling"** is an authentic record of work carried out by **aksha2005** (`akshakv2005@gmail.com`) for the course **DevOps (25CIAEC59 / 25CYAEC59)** under guidance.

---

## Abstract
This project demonstrates a complete, automated DevOps assembly line for a RESTful microservice application. The pipeline spans version control (Git with branching and merge strategy), continuous integration (Jenkins declarative pipeline and GitHub Actions), configuration management (Ansible inventory, playbooks, roles, and handlers), containerization (Docker with non-root security), and container orchestration (Kubernetes Deployment, Service, and ConfigMap with pod auto-healing and scaling). By unifying the application and deployment assets under a single repository (`devops-todo-app`), the workflow establishes seamless code-to-production automation.

---

# COMPONENT 1: CI/CD Pipeline with Git, Python & Jenkins (10 Marks)

## 1. Development Environment & Prerequisites
The development environment utilizes free and open-source tools:
- **Operating System**: Windows 11 / WSL2 Linux
- **Version Control**: Git (`git version 2.x`)
- **Runtime & Language**: Python 3.10+ / 3.11 with `Flask` and `pytest`
- **CI Server**: Jenkins Pipeline & GitHub Actions Runner
- **Container Runtime**: Docker Desktop / Docker Engine
- **Orchestration**: Kubernetes (`minikube` / `kind` compatible manifests)

---

## 2. Application Architecture & Implementation
The application is a lightweight RESTful Todo API written in Python using Flask. It exposes 5 core endpoints including a specialized `/health` endpoint for CI testing and Kubernetes liveness/readiness probes.

### Key Code Files

#### `app.py`
```python
import os
from flask import Flask, jsonify, request

app = Flask(__name__)

todos = [
    {"id": 1, "title": "Setup DevOps pipeline", "completed": True},
    {"id": 2, "title": "Deploy to Kubernetes", "completed": False}
]

@app.route('/', methods=['GET'])
def index():
    return jsonify({
        "message": "Welcome to DevOps Todo API",
        "endpoints": {"health": "/health", "todos": "/todos"}
    }), 200

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "UP"}), 200

@app.route('/todos', methods=['GET'])
def get_todos():
    return jsonify(todos), 200

@app.route('/todos', methods=['POST'])
def create_todo():
    data = request.get_json()
    if not data or 'title' not in data:
        return jsonify({"error": "Title is required"}), 400
    new_id = max([t['id'] for t in todos], default=0) + 1
    new_todo = {"id": new_id, "title": data['title'], "completed": data.get('completed', False)}
    todos.append(new_todo)
    return jsonify(new_todo), 201

@app.route('/todos/<int:todo_id>', methods=['PUT'])
def update_todo(todo_id):
    data = request.get_json()
    if not data:
        return jsonify({"error": "Invalid payload"}), 400
    todo = next((t for t in todos if t['id'] == todo_id), None)
    if not todo:
        return jsonify({"error": "Todo not found"}), 404
    if 'title' in data:
        todo['title'] = data['title']
    if 'completed' in data:
        todo['completed'] = data['completed']
    return jsonify(todo), 200

@app.route('/todos/<int:todo_id>', methods=['DELETE'])
def delete_todo(todo_id):
    global todos
    todo = next((t for t in todos if t['id'] == todo_id), None)
    if not todo:
        return jsonify({"error": "Todo not found"}), 404
    todos = [t for t in todos if t['id'] != todo_id]
    return jsonify({"message": f"Todo {todo_id} deleted successfully"}), 200

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)
```

#### `test_app.py`
Unit testing suite using `pytest` verifying endpoint functionality, status codes (200, 201, 400, 404), and health reporting.

---

## 3. Git Version Control & Branching Strategy
The repository features an explicit multi-branch workflow:
- **`main`**: Production-ready branch containing merged code.
- **`feature/k8s-configmap`**: Feature branch used to add Kubernetes ConfigMap resources before merging into `main`.

### Git Commit Log Output (`git log --graph`)
```text
*   009073f aksha2005 <akshakv2005@gmail.com> Merge branch 'feature/k8s-configmap' into main
|\  
| * e218433 aksha2005 <akshakv2005@gmail.com> feat: Add Kubernetes ConfigMap and connect to Deployment
|/  
* 1a59ca2 aksha2005 <akshakv2005@gmail.com> Initial commit: Core Flask Todo application and basic setup
```

---

## 4. Jenkins Pipeline Configuration (`Jenkinsfile`)
A declarative pipeline automates dependencies, testing, container creation, and artifact generation.

```groovy
pipeline {
    agent any

    environment {
        APP_NAME = 'devops-todo-app'
        IMAGE_TAG = "devops-todo-app:${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Install Dependencies') {
            steps {
                sh '''
                    python3 -m venv venv
                    . venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                '''
            }
        }
        stage('Run Unit Tests') {
            steps {
                sh '''
                    . venv/bin/activate
                    pytest --junitxml=test-results.xml
                '''
            }
        }
        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t ${APP_NAME}:latest -t ${IMAGE_TAG} .
                '''
            }
        }
    }

    post {
        always { junit allowEmptyResults: true, testResults: 'test-results.xml' }
    }
}
```

---

## 5. Component 1 CI Enhancements Implemented
We implemented **two** CI enhancements:
1. **Automated Testing in CI (`pytest`)**: Automated execution of unit tests prior to image packaging.
2. **GitHub Actions Workflow (`.github/workflows/ci.yml`)**: Parallel cloud-native CI automation running alongside Jenkins on every push and pull request.

---

# COMPONENT 2: Configuration Management (Ansible) + Kubernetes Deployment (10 Marks)

## 1. Ansible Configuration Management
Ansible automates server provisioning using roles, tasks, handlers, and variables.

- **Inventory (`ansible/inventory`)**: Defines target hosts and variables (`app_port=5000`, `docker_image=devops-todo-app:latest`).
- **Playbook (`ansible/playbook.yml`)**: Applies the `setup_app` role to target webservers.
- **Role Tasks (`ansible/roles/setup_app/tasks/main.yml`)**: Ensures Docker is running, pulls the application container image, and starts the container with port binding (`5000:5000`).
- **Handlers (`ansible/roles/setup_app/handlers/main.yml`)**: Restarts container on configuration changes.

---

## 2. Docker Containerization (`Dockerfile`)
```dockerfile
FROM python:3.11-slim
ENV PYTHONDONTWRITEBYTECODE=1 PYTHONUNBUFFERED=1 PORT=5000
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY app.py .
RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser
EXPOSE 5000
HEALTHCHECK --interval=30s --timeout=3s CMD python -c "import urllib.request; urllib.request.urlopen('http://localhost:5000/health')" || exit 1
CMD ["gunicorn", "--bind", "0.0.0.0:5000", "--workers", "2", "app:app"]
```

---

## 3. Kubernetes Orchestration & Scaling
The deployment includes three manifests in `k8s/`:
1. **`k8s/deployment.yaml`**: Configures 2 initial replicas, CPU/memory resource limits, and environment configuration.
2. **`k8s/service.yaml`**: Exposes the application via NodePort on port 30080 (routing port 80 -> 5000).
3. **`k8s/configmap.yaml`**: Externalized configuration key-value pairs (`PORT`, `APP_ENV`, `LOG_LEVEL`).

### Scaling Demonstration Commands
```bash
# Verify initial 2 replicas
kubectl get pods -l app=devops-todo-app

# Scale up to 5 replicas
kubectl scale deployment devops-todo-app --replicas=5

# Confirm 5 running pods
kubectl get pods -l app=devops-todo-app

# Scale back down to 2 replicas
kubectl scale deployment devops-todo-app --replicas=2
```

---

## 4. Component 2 Deployment Enhancements Implemented
1. **Liveness & Readiness Probes**: Probes hit HTTP `/health` on port 5000 to monitor container health and prevent routing traffic to unready pods.
2. **Kubernetes ConfigMap Integration**: Decoupled application settings from container code using `k8s/configmap.yaml` referenced via `envFrom`.

---

# Resume Framing (Student Template)

**DevOps Todo App — CI/CD Pipeline & Kubernetes Deployment** | [GitHub Repo](https://github.com/aksha2005/devops-todo-app.git)  
*Python (Flask), Git, Jenkins, GitHub Actions, Docker, Ansible, Kubernetes*  
- Built a REST API for task management (`GET /todos`, `POST /todos`, `GET /health`) and automated its build pipeline using Jenkins and GitHub Actions triggered on every Git push.  
- Integrated automated `pytest` test stage and GitHub Actions CI alongside Jenkins for dual-pipeline validation.  
- Containerized application with Docker (slim Python, non-root user) and deployed on Kubernetes with 2 initial replicas and NodePort routing.  
- Configured server environment using Ansible playbook with modular roles, handlers, and inventory variables.  
- Enhanced deployment resiliency with HTTP liveness/readiness health probes, Kubernetes ConfigMap integration, and zero-downtime scaling (`kubectl scale`).

---

## Conclusion
The `devops-todo-app` assignment successfully demonstrates the end-to-end DevOps workflow. Every stage—from local Python development and Git version control, to Jenkins and GitHub Actions CI automation, Ansible server configuration, Docker container packaging, and Kubernetes deployment scaling—operates as a cohesive pipeline.

---

## References
1. Flask Documentation: [https://flask.palletsprojects.com/](https://flask.palletsprojects.com/)
2. Jenkins Declarative Pipeline Syntax: [https://www.jenkins.io/doc/book/pipeline/](https://www.jenkins.io/doc/book/pipeline/)
3. Ansible Role Architecture: [https://docs.ansible.com/ansible/latest/playbook_guide/playbooks_reuse_roles.html](https://docs.ansible.com/ansible/latest/playbook_guide/playbooks_reuse_roles.html)
4. Kubernetes Deployments & Probes: [https://kubernetes.io/docs/concepts/workloads/controllers/deployment/](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
