import pytest
from app import app

@pytest.fixture
def client():
    app.config['TESTING'] = True
    with app.test_client() as client:
        yield client

def test_index(client):
    response = client.get('/')
    assert response.status_code == 200
    data = response.get_json()
    assert "Welcome to DevOps Todo API" in data["message"]

def test_health_check(client):
    response = client.get('/health')
    assert response.status_code == 200
    data = response.get_json()
    assert data["status"] == "UP"

def test_get_todos(client):
    response = client.get('/todos')
    assert response.status_code == 200
    data = response.get_json()
    assert isinstance(data, list)
    assert len(data) >= 1

def test_create_todo(client):
    new_item = {"title": "Automate Ansible testing", "completed": False}
    response = client.post('/todos', json=new_item)
    assert response.status_code == 201
    data = response.get_json()
    assert data["title"] == "Automate Ansible testing"
    assert data["completed"] is False
    assert "id" in data

def test_create_todo_missing_title(client):
    response = client.post('/todos', json={})
    assert response.status_code == 400
    data = response.get_json()
    assert "error" in data

def test_update_todo(client):
    update_data = {"completed": True}
    response = client.put('/todos/1', json=update_data)
    assert response.status_code == 200
    data = response.get_json()
    assert data["completed"] is True

def test_update_todo_not_found(client):
    response = client.put('/todos/9999', json={"title": "Non-existent"})
    assert response.status_code == 404

def test_delete_todo(client):
    response = client.delete('/todos/1')
    assert response.status_code == 200
    data = response.get_json()
    assert "deleted successfully" in data["message"]

def test_delete_todo_not_found(client):
    response = client.delete('/todos/9999')
    assert response.status_code == 404
