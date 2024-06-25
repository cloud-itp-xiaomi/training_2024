import { connect } from "react-redux";
import { toggleTodo } from "../actions";
import TodoList from "../components/TodoList";

// 根据过滤器获取可见的待办事项列表
const getVisibleTodos = (todos, filter) => {
  switch (filter) {
    case "SHOW_ALL":
      return todos;
    case "SHOW_COMPLETED":
      return todos.filter((t) => t.completed);
    case "SHOW_ACTIVE":
      return todos.filter((t) => !t.completed);
    default:
      throw new Error("Unknown filter: " + filter);
  }
};

// 定义 mapStateToProps 函数，映射 state 到组件的 props
const mapStateToProps = (state) => ({
  todos: getVisibleTodos(state.todos.present, state.visibilityFilter),
});

// 定义 mapDispatchToProps 对象，映射 dispatch 到组件的 props
const mapDispatchToProps = {
  onTodoClick: toggleTodo, // 映射切换待办事项完成状态的动作
};

// 将 TodoList 组件连接到 Redux store
const VisibleTodoList = connect(mapStateToProps, mapDispatchToProps)(TodoList);

// 导出 VisibleTodoList 容器组件
export default VisibleTodoList;
