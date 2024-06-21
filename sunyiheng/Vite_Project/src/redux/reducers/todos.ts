import undoable from "redux-undo";
import {
  ADD_TODO,
  TOGGLE_TODO,
  CHECK_ALL,
  CLEAR_COMPLETED,
  DELETE_TODO,
} from "../constant";
/**
 * 数据类型：
 * {
 *  id: nanoid(),
 *  todo: string,
 *  completed: boolean,
 * }
 *
 * 该Reducer主要是针对每一项待办事项所执行的操作
 *
 * 作用：1. 向待办事项列表添加新的待办事项;
 *       2. 根据id来更改待办事项的完成状态
 */
const iniState = []; // ToDoList，初始状态为[]，根据旧的state和action，返回新的state
const todos = (previousState = iniState, action) => {
  const { type, data } = action;
  switch (type) {
    case ADD_TODO:
      return [...previousState, data];
    case TOGGLE_TODO:
      return previousState.map((todo) =>
        todo.id === data ? { ...todo, completed: !todo.completed } : todo
      );
    case CHECK_ALL:
    case CLEAR_COMPLETED:
      return data;
    case DELETE_TODO:
      return previousState.filter((todo) => todo.id !== data);
    default:
      return previousState;
  }
};

const undoableTodos = undoable(todos);

export default undoableTodos;
