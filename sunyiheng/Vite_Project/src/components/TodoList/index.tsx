import React from "react";
import "./index.css";
import { Button } from "antd";

function TodoList({ todos, handleChecked, handleDelete }) {
  return (
    <ul style={{ listStyle: "none" }}>
      {todos.map((todo) => {
        return (
          <li key={todo.id}>
            <label>
              <input
                type="checkbox"
                checked={todo.completed}
                onChange={() => handleChecked(todo.id)}
              />
              <span>{todo.todo}</span>
              <Button
                danger
                onClick={() => handleDelete(todo.id)}
                className="btn"
              >
                删除
              </Button>
            </label>
          </li>
        );
      })}
    </ul>
  );
}

export default TodoList;
