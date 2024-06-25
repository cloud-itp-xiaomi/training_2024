// 初始化一个待办事项的ID，从0开始
let nextTodoId = 0;

// 定义添加待办事项的动作类型常量
export const ADD_TODO = "ADD_TODO";
// 定义切换待办事项完成状态的动作类型常量
export const TOGGLE_TODO = "TOGGLE_TODO";
// 定义设置可见性过滤器的动作类型常量
export const SET_VISIBILITY_FILTER = "SET_VISIBILITY_FILTER";

// 定义添加待办事项的动作创建函数
export const addTodo = (text) => ({
  type: ADD_TODO, // 动作类型为添加待办事项
  id: nextTodoId++, // 为新待办事项生成唯一ID并递增
  text, // 动作携带的待办事项内容
});

// 定义切换待办事项完成状态的动作创建函数
export const toggleTodo = (id) => ({
  type: TOGGLE_TODO, // 动作类型为切换待办事项状态
  id, // 动作携带的待办事项ID
});

// 定义设置可见性过滤器的动作创建函数
export const setVisibilityFilter = (filter) => ({
  type: SET_VISIBILITY_FILTER, // 动作类型为设置可见性过滤器
  filter, // 动作携带的过滤器类型
});
