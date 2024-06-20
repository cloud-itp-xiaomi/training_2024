import { ADD_TODO, TOGGLE_TODO } from "../constant";
/**
 * 数据类型：
 * {
 *  id: nanoid(),
 *  todo: string,
 *  completed: boolean,
 * }
 *
 * 作用：1. 向待办事项列表添加新的待办事项;
 *       2. 根据id来更改待办事项的完成状态
 */
const iniState = []; // ToDoList，初始状态为[]，根据旧的state和action，返回新的state
export default function (previousState = iniState, action) {
  const { type, data } = action;
  switch (type) {
    case ADD_TODO:
      return [...previousState, data];
    case TOGGLE_TODO:
      return previousState.map((todo) =>
        todo.id === data ? { ...todo, completed: !todo.completed } : todo
      );
    default:
      return previousState;
  }
}
