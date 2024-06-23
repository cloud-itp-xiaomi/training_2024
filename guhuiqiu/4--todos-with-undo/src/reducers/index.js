// 从 'redux' 库中导入 combineReducers 函数，用于组合多个 reducer
import { combineReducers } from "redux";
// 导入 todos reducer
import todos from "./todos";
// 导入 visibilityFilter reducer
import visibilityFilter from "./visibilityFilter";

// 使用 combineReducers 函数组合 todos 和 visibilityFilter reducer
const todoApp = combineReducers({
  todos, // 待办事项的 reducer
  visibilityFilter, // 可见性过滤器的 reducer
});

// 导出组合后的 reducer
export default todoApp;
