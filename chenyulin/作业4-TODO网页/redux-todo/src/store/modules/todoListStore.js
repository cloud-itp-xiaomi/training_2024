import { createSlice } from "@reduxjs/toolkit";

const todoListStore = createSlice({
  name: "todoList",
  initialState: { todos: [] },
  reducers: {
    // 增加清单到页面中
    add: (state, action) => {
      state.todos.push(action.payload);
    },
  },
});

export const { add } = todoListStore.actions;

export default todoListStore.reducer;
