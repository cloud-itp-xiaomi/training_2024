import React, { useRef } from "react";
import { add_todo } from "../../redux/actions/";
import { connect } from "react-redux";
import { nanoid } from "nanoid";

function Header(props) {
  const { todos, add_todo } = props;

  const inputRef = useRef(null);

  const addTodo = () => {
    const todo = inputRef.current.value;
    const newTodo = {
      id: nanoid(),
      todo,
      completed: false,
    };
    if (todo.trim() === "") {
      alert("输入不能为空");
      return;
    }
    add_todo(newTodo);
    inputRef.current.value = "";
  };

  return (
    <>
      <form onSubmit={(e) => e.preventDefault()}>
        <input type="text" ref={inputRef} />
        &nbsp;
        <button onClick={addTodo}>点击添加待办事项</button>
      </form>
    </>
  );
}

export default connect(
  (state) => ({
    todos: state.todos,
  }),
  { add_todo }
)(Header);
