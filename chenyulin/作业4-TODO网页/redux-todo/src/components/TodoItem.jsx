// 每个清单详情

import { useDispatch } from "react-redux";
import { toggle } from "../store/modules/todoListStore";
import classes from "./TodoItem.module.css";

function TodoItem({ todo }) {
  const dispatch = useDispatch();
  // 切换完成状态
  const handleToggleTodo = (id) => {
    dispatch(toggle(id));
  };

  return (
    <>
      <li>
        {/* 复选框 */}
        <input
          className={classes.check}
          type="checkbox"
          checked={todo.complete}
          onChange={() => handleToggleTodo(todo.id)}
        />
        {/* 清单列表 */}
        <span
          onClick={() => handleToggleTodo(todo.id)}
          style={{
            textDecoration: todo.complete ? "line-through" : "none",
          }}
        >
          {todo.text}
        </span>
      </li>
    </>
  );
}

export default TodoItem;
