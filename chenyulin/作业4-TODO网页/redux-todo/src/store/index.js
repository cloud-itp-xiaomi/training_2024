import { configureStore } from "@reduxjs/toolkit";
import todoListStore from "./modules/todoListStore";
const store = configureStore({
  reducer: {
    todoList: todoListStore,
  },
});
export default store;
