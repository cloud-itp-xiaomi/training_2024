// eslint-disable-next-line no-unused-vars
import React from "react";
import { connect } from "react-redux";
import { addTodo } from "../actions";

// 定义 AddTodo 容器组件，用于添加新待办事项
// eslint-disable-next-line react/prop-types
let AddTodo = ({ dispatch }) => {
  let input;

  return (
    <div>
      <form
        onSubmit={(e) => {
          e.preventDefault(); // 阻止表单默认提交行为
          if (!input.value.trim()) {
            return; // 如果输入为空则不添加待办事项
          }
          dispatch(addTodo(input.value)); // 分发 addTodo 动作
          input.value = ""; // 清空输入框
        }}
      >
        <input
          ref={(node) => {
            input = node; // 绑定输入框引用
          }}
        />
        <button type="submit">Add Todo</button>
      </form>
    </div>
  );
};
// 将 AddTodo 组件连接到 Redux store
AddTodo = connect()(AddTodo);

// 导出 AddTodo 容器组件
export default AddTodo;
