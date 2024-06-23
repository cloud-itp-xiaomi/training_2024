import React from "react";
import PropTypes from "prop-types";
import Todo from "./Todo";

// 定义 TodoList 组件，用于渲染待办事项列表
const TodoList = ({ todos, onTodoClick }) => (
  <ul>
    {/* 遍历待办事项数组并渲染每个待办事项 */}
    {todos.map((todo) => (
      <Todo key={todo.id} {...todo} onClick={() => onTodoClick(todo.id)} />
    ))}
  </ul>
);

// 定义 TodoList 组件的属性类型
TodoList.propTypes = {
  todos: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired, // 待办事项的唯一ID
      completed: PropTypes.bool.isRequired, // 是否完成
      text: PropTypes.string.isRequired, // 待办事项内容
    }).isRequired
  ).isRequired,
  onTodoClick: PropTypes.func.isRequired, // 待办事项点击处理函数
};

// 导出 TodoList 组件
export default TodoList;
