# setup_app role

Deploys the DevOps Todo Flask application as a Docker container.

## What it does
1. Checks that the Docker daemon is reachable.
2. Checks that the local image exists (and stops if it does not).
3. Starts the container with an always-restart policy and publishes the app port.
4. Notifies a handler that calls `/health` and expects HTTP 200.

## Variables (defaults/main.yml, overridden by the inventory)
| Variable | Default | Meaning |
|---|---|---|
| app_name | devops-todo-app | Container name |
| app_port | 5000 | Host port mapped to container port 5000 |
| docker_image | devops-todo-app:latest | Image to run |

## Usage
    ansible-playbook -i ansible/inventory ansible/playbook.yml
