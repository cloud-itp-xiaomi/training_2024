// 从 'redux-undo' 库中导入 undoable 函数，用于创建可撤销的 reducer
import undoable from "redux-undo";

// 定义单个待办事项的 reducer 函数 'todo'
const todo = (state, action) => {
  switch (action.type) {
    case "ADD_TODO":
      // 处理添加待办事项的动作，返回新待办事项对象
      return {
        id: action.id, // 待办事项的唯一ID
        text: action.text, // 待办事项的内容
        completed: false, // 待办事项的初始完成状态为未完成
      };
    case "TOGGLE_TODO":
      // 处理切换待办事项完成状态的动作
      if (state.id !== action.id) {
        return state; // 如果待办事项ID不匹配，返回原状态
      }

      // 返回切换完成状态后的新对象
      return {
        ...state, // 保留原状态的其他属性
        completed: !state.completed, // 切换 completed 属性
      };
    default:
      // 默认返回当前状态
      return state;
  }
};

// 定义待办事项列表的 reducer 函数 'todos'
const todos = (state = [], action) => {
  switch (action.type) {
    case "ADD_TODO":
      // 处理添加待办事项的动作，返回新状态数组
      return [...state, todo(undefined, action)]; // 将新待办事项添加到状态数组中
    case "TOGGLE_TODO":
      // 处理切换待办事项完成状态的动作
      return state.map((t) => todo(t, action)); // 更新匹配ID的待办事项的状态
    default:
      // 默认返回当前状态
      return state;
  }
};

// 使用 undoable 函数创建可撤销的 todos reducer
const undoableTodos = undoable(todos);

// 导出可撤销的 todos reducer
export default undoableTodos;
