import React, { useRef, useState } from "react";
import { add_todo } from "../../redux/actions/";
import { connect } from "react-redux";
import { nanoid } from "nanoid";
import { Button, Input, Space } from "antd";
import "./index.css";

function Header({ add_todo }) {
  // 受控组件的实现
  const [inputValue, setInputValue] = useState("");

  const addTodo = () => {
    const newTodo = {
      id: nanoid(),
      todo: inputValue,
      completed: false,
    };
    if (inputValue.trim() === "") {
      alert("输入不能为空");
      return;
    }
    add_todo(newTodo);
    setInputValue("");
  };

  return (
    <>
      <div className="title">
        <h1>带有撤销功能的待办事项</h1>
        <img src="待办事项.svg"></img>
      </div>
      <form onSubmit={(e) => e.preventDefault()}>
        {/* <input type="text" ref={inputRef} />
        &nbsp;
        <button onClick={addTodo}>点击添加待办事项</button> */}
        <Space.Compact style={{ width: "100%" }}>
          <Input
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
          />
          <Button type="primary" onClick={addTodo}>
            点击添加待办事项
          </Button>
        </Space.Compact>
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
