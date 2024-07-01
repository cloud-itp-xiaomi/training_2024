import {
  ADD_TODO,
  TOGGLE_TODO,
  CHECK_ALL,
  CLEAR_COMPLETED,
  DELETE_TODO,
} from "../constant";

// 接收用户输入的待办事项，组装成 add_todo creator，向待办列表中添加新的待办事项
// 单个 todo 的数据格式：{id, todo, completed}
export const add_todo = (TodoObj) => ({ type: ADD_TODO, data: TodoObj });

// 接收id，通过对比待办事项的id，实现完成状态的变化
export const toggle_todo = (id) => ({ type: TOGGLE_TODO, data: id });

// 全选
export const check_all = (newTodos) => ({ type: CHECK_ALL, data: newTodos });

// 清除全选
export const clear_completed = (newTodos) => ({
  type: CLEAR_COMPLETED,
  data: newTodos,
});

// 删除单个todo
export const delete_todo = (id) => ({ type: DELETE_TODO, data: id });
