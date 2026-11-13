# DevOps Pipeline & Kubernetes Deployment Report

**Course Code:** 25CIAEC59 / 25CYAEC59  
**Course Title:** DevOps  
**Credits:** 1:0:0 (Contact Hours: 15L)  
**Total Marks:** 20 (Component 1: 10 Marks | Component 2: 10 Marks)

---

# Student Details

- **Student Name:** Aksha K V
- **Branch:** Computer Science and Engineering (Cyber Security)
- **GitHub Repository:** https://github.com/aksha2005/devops-todo-app
- **Submission Date:** November 3, 2026

---

# Certificate and Declaration

This is to certify that the project titled **"DevOps Todo Application: CI Pipeline, Ansible Automation, Docker Containerization, and Kubernetes Deployment"** is an authentic record of work carried out as part of the DevOps course.

The project demonstrates practical implementation of version control, continuous integration, automated testing, containerization, configuration management, and container orchestration.

---

# Abstract

This project demonstrates an end-to-end DevOps workflow using a Python Flask-based Todo REST API. The application is developed and maintained using Git and GitHub, tested through automated testing with Pytest, and integrated with Jenkins and GitHub Actions for Continuous Integration (CI).

Docker is used to containerize the application. Ansible automates container deployment through inventory files, playbooks, reusable roles, variables, and handlers. Kubernetes manages the application using Deployments, Services, and ConfigMaps.

The Kubernetes deployment includes three initial replicas, health probes, resource requests and limits, and a scaling demonstration from three replicas to five replicas and back to three.

The project demonstrates how development, testing, containerization, configuration management, and deployment technologies work together in a DevOps environment.

---

# COMPONENT 1: CI Pipeline with Git, Python and Jenkins (10 Marks)

## 1. Development Environment and Prerequisites

The project uses the following tools and technologies:

| Technology | Purpose |
|---|---|
| Windows 11 | Host operating system |
| Ubuntu 24.04 WSL2 | Linux development environment |
| Python | Backend programming |
| Flask | REST API development |
| Pytest | Automated testing |
| Git | Version control |
| GitHub | Source code repository |
| Jenkins | Continuous Integration |
| GitHub Actions | Additional CI workflow |
| Docker | Application containerization |
| Kubernetes | Container orchestration |
| Minikube | Local Kubernetes cluster |
| Ansible | Configuration management and automation |

## 2. Application Architecture and Implementation

The DevOps Todo Application is a REST API developed using Python and Flask.

It allows users to create, retrieve, update, and delete Todo tasks. It also provides a health-check endpoint to verify application availability.

### Main Application File: `app.py`

The application provides the following endpoints:

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/` | Displays application information |
| GET | `/health` | Checks application health |
| GET | `/todos` | Retrieves all Todo tasks |
| POST | `/todos` | Creates a new Todo task |
| PUT | `/todos/<id>` | Updates an existing Todo task |
| DELETE | `/todos/<id>` | Deletes a Todo task |

### Application Features

- Create new Todo tasks.
- Retrieve all Todo tasks.
- Update existing Todo tasks.
- Delete Todo tasks.
- Validate missing or invalid task information.
- Provide an application health-check endpoint.

### Dependencies

The project dependencies are maintained in `requirements.txt`.

The application uses Flask for REST API development, Gunicorn for production container execution, and Pytest for automated testing.

## 3. Git Version Control and Branching Strategy

Git and GitHub are used to maintain the source code and track project changes.

The repository uses the following branches:

- **`main`:** Main branch containing the integrated project implementation.
- **`feature/k8s-configmap`:** Feature branch used to implement Kubernetes ConfigMap functionality.

### Branching and Merging Process

1. The main application was maintained in the `main` branch.
2. A separate feature branch named `feature/k8s-configmap` was used to implement Kubernetes ConfigMap functionality.
3. The ConfigMap was connected to the Kubernetes Deployment.
4. The feature changes were integrated into the `main` branch through a Git merge commit.

The GitHub commit history contains the merge commit:

`Merge branch 'feature/k8s-configmap' into main`

This demonstrates the use of feature branching and merging in Git.

### Git Commands

```bash
# View available branches
git branch

# Create a feature branch
git checkout -b feature/k8s-configmap

# Stage changes
git add .

# Commit changes
git commit -m "Add Kubernetes ConfigMap"

# Switch to main
git checkout main

# Merge feature branch
git merge feature/k8s-configmap

# View commit history
git log --oneline --graph --all
```

## 4. Jenkins Continuous Integration Pipeline

Jenkins is used to automate the Continuous Integration process.

A Jenkins Pipeline job named `project` is configured to retrieve source code from the GitHub repository.

### Jenkins Configuration

- **Repository:** `https://github.com/aksha2005/devops-todo-app.git`
- **Branch:** `main`
- **Pipeline Script:** `Jenkinsfile`
- **SCM Trigger:** Poll SCM
- **Polling Interval:** Every two minutes using `H/2 * * * *`

The Poll SCM configuration periodically checks the GitHub repository for changes. When changes are detected, Jenkins automatically starts a new pipeline build.

### Jenkins Pipeline Stages

The Jenkinsfile contains the following stages:

1. **Checkout:** Retrieves the source code from GitHub.
2. **Install Dependencies:** Creates a Python virtual environment and installs dependencies from `requirements.txt`.
3. **Run Unit Tests:** Executes automated tests using Pytest.
4. **Generate Build Information:** Creates a `build-info.txt` file containing the application name, build number, and Git commit information.

### Jenkins Post-Build Actions

After the pipeline execution:

- JUnit test results are published.
- Build artifacts are archived.
- The Python virtual environment is removed.
- A success or failure message is displayed.

### Archived Artifacts

The following artifacts are generated and archived:

| Artifact | Purpose |
|---|---|
| `test-results.xml` | Stores automated test results in JUnit XML format |
| `build-info.txt` | Stores application name, build number, and Git commit information |

### Jenkins Execution Results

The Jenkins pipeline successfully executed the following activities:

- Source code checkout.
- Python dependency installation.
- Automated test execution.
- Build information generation.
- JUnit test result publication.
- Artifact archiving.

The successful Jenkins builds demonstrate the working CI pipeline.

## 5. Automated Testing

Pytest is used to perform automated testing of the Flask REST API.

The test cases verify application routes, health checks, Todo creation, retrieval, updating, deletion, and invalid requests.

### Test Cases

1. `test_index`
2. `test_health_check`
3. `test_get_todos`
4. `test_create_todo`
5. `test_create_todo_missing_title`
6. `test_update_todo`
7. `test_update_todo_not_found`
8. `test_delete_todo`
9. `test_delete_todo_not_found`

### Test Execution

```bash
pytest -v
```

The Jenkins pipeline executes the tests automatically and generates the JUnit report.

**Result:** All 9 automated tests passed in the successful Jenkins build.

## 6. Component 1 Enhancements Implemented

The following enhancements were implemented:

### Enhancement 1: Automated Testing

Pytest is integrated into the Jenkins pipeline. Tests are executed automatically during the CI process, and their results are published in JUnit XML format.

### Enhancement 2: GitHub Actions CI

A GitHub Actions workflow is configured in:

`.github/workflows/ci.yml`

The workflow provides an additional automated CI process for the repository.

Three successful workflow runs were observed in GitHub Actions.

### Enhancement 3: Jenkins Artifact Archiving

Jenkins automatically archives:

- `test-results.xml`
- `build-info.txt`

This provides access to test reports and build information after pipeline execution.

---

# COMPONENT 2: Ansible Configuration Management and Kubernetes Deployment (10 Marks)

## 1. Ansible Configuration Management

Ansible is used to automate the deployment of the Todo application container.

The implementation uses an inventory file, a playbook, a reusable role, variables, tasks, and a handler.

### Ansible Inventory

File: `ansible/inventory`

The inventory defines the target host and application configuration variables.

Example configuration:

```ini
[webservers]
localhost ansible_connection=local

[all:vars]
ansible_python_interpreter=/usr/bin/python3
app_name=devops-todo-app
app_port=5000
docker_image=devops-todo-app:latest
```

### Ansible Playbook

File: `ansible/playbook.yml`

The playbook executes the `setup_app` role on the configured webservers.

```yaml
---
- name: Deploy DevOps Todo Application
  hosts: webservers
  become: true
  roles:
    - setup_app
```

### Ansible Role Tasks

File: `ansible/roles/setup_app/tasks/main.yml`

The role performs the following tasks:

1. Verifies that the Docker daemon is accessible.
2. Checks whether the local Docker image exists.
3. Starts the Todo application container.
4. Configures the container restart policy.
5. Maps host port 5000 to container port 5000.

The implementation uses the existing local Docker image and does not pull the image from a remote registry.

### Ansible Handler

File: `ansible/roles/setup_app/handlers/main.yml`

A handler named `Restart Todo Application Container` is configured to restart the application container when notified by a task.

### Running the Playbook

```bash
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

### Ansible Verification

The playbook was successfully executed.

The following checks were performed:

- Ansible syntax check completed successfully.
- Docker daemon accessibility was verified.
- Local Docker image availability was verified.
- Application container was started successfully.
- Container health was checked using the `/health` endpoint.
- A second playbook execution completed with zero changes, demonstrating idempotency.

## 2. Docker Containerization

Docker is used to package the Flask application and its dependencies into a container image.

### Dockerfile

The project uses the official Python 3.11 slim image as its base image.

The Dockerfile performs the following operations:

1. Selects Python 3.11 slim as the base image.
2. Configures environment variables.
3. Creates the `/app` working directory.
4. Copies and installs application dependencies.
5. Copies the Flask application.
6. Creates a non-root application user.
7. Exposes port 5000.
8. Configures a Docker health check.
9. Starts the application using Gunicorn.

### Dockerfile Configuration

```dockerfile
FROM python:3.11-slim

ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    PORT=5000

WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY app.py .

RUN useradd -m appuser && chown -R appuser:appuser /app

USER appuser

EXPOSE 5000

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD python -c "import urllib.request; urllib.request.urlopen('http://localhost:5000/health')" || exit 1

CMD ["gunicorn", "--bind", "0.0.0.0:5000", "--workers", "2", "app:app"]
```

### Docker Commands

Build the Docker image:

```bash
docker build -t devops-todo-app:latest .
```

Run the container:

```bash
docker run -d -p 5000:5000 --name devops-todo-app devops-todo-app:latest
```

Verify the running container:

```bash
docker ps
```

The Docker image was built successfully, and the application container was verified as running and healthy.

## 3. Kubernetes Deployment and Orchestration

Kubernetes is used to deploy, expose, monitor, and scale the Todo application.

The project uses Minikube to run a local Kubernetes cluster.

Three Kubernetes manifest files are maintained in the `k8s/` directory.

### A. Deployment: `k8s/deployment.yaml`

The Deployment manages the application pods.

Configuration includes:

- Three initial replicas.
- Container image: `devops-todo-app:latest`.
- Container port: 5000.
- CPU and memory requests.
- CPU and memory limits.
- ConfigMap-based environment configuration.
- Liveness probe.
- Readiness probe.

### B. Service: `k8s/service.yaml`

A NodePort Service is used to expose the application.

The service configuration uses:

- Service type: NodePort.
- Service port: 80.
- Target port: 5000.
- NodePort: 30080.

The Service forwards incoming traffic to the application containers.

### C. ConfigMap: `k8s/configmap.yaml`

The ConfigMap stores application configuration separately from the application code.

The following configuration values are maintained:

| Key | Value |
|---|---|
| PORT | 5000 |
| APP_ENV | production |
| LOG_LEVEL | INFO |

The Deployment references the ConfigMap using `envFrom`.

This allows application configuration to be maintained separately from the container image.

## 4. Kubernetes Health Monitoring

The Kubernetes Deployment uses liveness and readiness probes to monitor application health.

### Liveness Probe

The liveness probe checks the `/health` endpoint on port 5000.

It helps Kubernetes identify containers that are no longer functioning correctly.

### Readiness Probe

The readiness probe checks the same health endpoint.

It determines whether a pod is ready to receive application traffic.

These probes improve application monitoring and help Kubernetes manage unhealthy or unready containers.

## 5. Kubernetes Scaling Demonstration

The application was initially deployed with three replicas.

The scaling process was demonstrated using Kubernetes commands.

### Step 1: Verify Initial Three Replicas

```bash
kubectl get pods -l app=devops-todo-app
```

**Result:** Three application pods were running.

### Step 2: Scale Up to Five Replicas

```bash
kubectl scale deployment devops-todo-app --replicas=5
```

Verify the pods:

```bash
kubectl get pods -l app=devops-todo-app
```

**Result:** Five application pods were running.

### Step 3: Scale Back to Three Replicas

```bash
kubectl scale deployment devops-todo-app --replicas=3
```

Verify the deployment:

```bash
kubectl get pods -l app=devops-todo-app
```

**Result:** The application was scaled back to three running pods.

### Kubernetes Verification Commands

```bash
kubectl get nodes

kubectl get deployments

kubectl get pods

kubectl get services

kubectl rollout status deployment/devops-todo-app

kubectl describe deployment devops-todo-app
```

The Kubernetes deployment and Service were verified successfully.

The application health endpoint returned:

```json
{
  "status": "UP"
}
```

The Todo API was also accessed through the Kubernetes Service.

## 6. Component 2 Deployment Enhancements Implemented

The following enhancements were implemented:

### Enhancement 1: Kubernetes ConfigMap Integration

Application configuration values are stored in a ConfigMap instead of being maintained entirely within the application deployment configuration.

The ConfigMap is referenced by the Deployment using `envFrom`.

### Enhancement 2: Liveness and Readiness Probes

HTTP health checks are configured using the `/health` endpoint.

These probes help Kubernetes monitor container health and readiness.

### Additional Configuration

- CPU and memory requests and limits.
- Docker non-root application user.
- Docker health check.
- Container restart policy.
- Ansible playbook idempotency verification.

---

# Project Architecture and Workflow

The project follows this DevOps workflow:

1. Develop the Todo REST API using Python and Flask.
2. Maintain source code using Git and GitHub.
3. Use feature branching and merge changes into `main`.
4. Trigger Jenkins CI through Poll SCM.
5. Install dependencies and execute automated tests.
6. Publish test reports and archive build artifacts.
7. Run additional CI checks using GitHub Actions.
8. Build the application Docker image.
9. Use Ansible to automate container deployment.
10. Deploy the application to Kubernetes using Minikube.
11. Expose the application using a NodePort Service.
12. Monitor application health using Kubernetes probes.
13. Scale the application from three replicas to five and back to three.

---

# Conclusion

The DevOps Todo Application demonstrates the practical implementation of a DevOps workflow using commonly used development and deployment tools.

Component 1 demonstrates Git version control, feature branching and merging, REST API development, Jenkins Continuous Integration, automated testing, GitHub Actions, and artifact archiving.

Component 2 demonstrates Docker containerization, Ansible configuration management, Kubernetes Deployment and Service configuration, ConfigMap integration, health monitoring, and replica scaling.

The project successfully demonstrates how an application can be developed, tested, packaged, deployed, monitored, and scaled using DevOps tools and practices.

---

# References

1. Flask Documentation: https://flask.palletsprojects.com/
2. Jenkins Pipeline Documentation: https://www.jenkins.io/doc/book/pipeline/
3. Ansible Documentation: https://docs.ansible.com/ansible/latest/playbook_guide/playbooks_reuse_roles.html
4. Docker Documentation: https://docs.docker.com/
5. Kubernetes Deployments: https://kubernetes.io/docs/concepts/workloads/controllers/deployment/
6. Kubernetes Probes: https://kubernetes.io/docs/concepts/configuration/liveness-readiness-startup-probes/
7. GitHub Actions Documentation: https://docs.github.com/en/actions
