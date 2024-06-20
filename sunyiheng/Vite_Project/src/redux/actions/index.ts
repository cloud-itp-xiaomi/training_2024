import { ADD_TODO, TOGGLE_TODO, CHANGE_VISIBILITY } from "../constant";

// 接收用户输入的待办事项，组装成 add_todo creator，向待办列表中添加新的待办事项
// 单个 todo 的数据格式：{id, todo, completed}
export const add_todo = (TodoObj) => ({ type: ADD_TODO, data: TodoObj });

// 接收id，通过对比待办事项的id，实现完成状态的变化
export const toggle_todo = (id) => ({ type: TOGGLE_TODO, data: id });
