
import PropTypes from "prop-types";

// 定义 Todo 组件，用于渲染单个待办事项
const Todo = ({ onClick, completed, text }) => (
  <li
    onClick={onClick} // 点击待办事项时调用传递的点击处理函数
    style={{
      textDecoration: completed ? "line-through" : "none", // 根据完成状态设置文本样式
    }}
  >
    {text}
  </li> // 显示待办事项的内容
);

// 定义 Todo 组件的属性类型
Todo.propTypes = {
  onClick: PropTypes.func.isRequired, // 点击处理函数
  completed: PropTypes.bool.isRequired, // 是否完成
  text: PropTypes.string.isRequired, // 待办事项内容
};

// 导出 Todo 组件
export default Todo;
