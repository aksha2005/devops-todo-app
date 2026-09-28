import os
from flask import Flask, jsonify, request

app = Flask(__name__)

# In-memory database for todos
todos = [
    {"id": 1, "title": "Setup DevOps pipeline", "completed": True},
    {"id": 2, "title": "Deploy to Kubernetes", "completed": False}
]

@app.route('/', methods=['GET'])
def index():
    return jsonify({
        "message": "Welcome to DevOps Todo API",
        "endpoints": {
            "health": "/health",
            "todos": "/todos"
        }
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
    new_todo = {
        "id": new_id,
        "title": data['title'],
        "completed": data.get('completed', False)
    }
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
