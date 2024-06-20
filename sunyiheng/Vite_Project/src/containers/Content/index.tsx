import React from "react";
import { connect } from "react-redux";
import { toggle_todo } from "../../redux/actions";

function Content({ todos, toggle_todo }) {
  const handleChecked = (id) => {
    toggle_todo(id);
  };

  return (
    <div>
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
              </label>
            </li>
          );
        })}
      </ul>
    </div>
  );
}

export default connect(
  (state) => ({
    todos: state.todos,
  }),
  { toggle_todo }
)(Content);
