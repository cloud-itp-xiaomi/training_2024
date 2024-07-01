import { configureStore } from "@reduxjs/toolkit";
import todos from "./reducers/todos";

export default configureStore({
  reducer: {
    todos,
  },
});
