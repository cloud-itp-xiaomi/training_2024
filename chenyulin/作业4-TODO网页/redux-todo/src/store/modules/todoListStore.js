import { createSlice } from "@reduxjs/toolkit";
import undoable, { includeAction } from "redux-undo";

const todoListStore = createSlice({
  name: "todoList",
  initialState: { todos: [] },
  reducers: {
    // 增加清单到页面中
    add: (state, action) => {
      state.todos.push(action.payload);
    },
    // 切换清单的完成状态
    toggle: (state, action) => {
      // 筛选出要修改的id的索引
      const index = state.todos.findIndex((todo) => todo.id === action.payload);
      // 检查是否找到了（index 不为-1表示找到了）
      if (index !== -1) {
        // 切换状态
        state.todos[index].complete = !state.todos[index].complete;
      }
    },
  },
});

export const { add, toggle } = todoListStore.actions;

export default undoable(todoListStore.reducer, {
  filter: includeAction([add.type, toggle.type]),
});
