// 整个清单列表界面

import { useSelector } from "react-redux";
import TodoInput from "./TodoInput";
import { useState } from "react";
import TodoMenu from "./TodoMenu";
import TodoItem from "./TodoItem";
import TodoUndoRedo from "./TodoUndoRedo";
import classes from "./TodoList.module.css";

function TodoList() {
  // useSelector()用来加载state中的数据
  const todos = useSelector((state) => state.todoList.present.todos);
  // 筛选不同状态的清单列表
  const [filteredTodos, setfilteredTodos] = useState(todos);

  return (
    <>
      <div className={classes.todolist}>
        <div className={classes.list}>
          <h1>陈雨霖的清单列表</h1>

          {/* 输入框 */}
          <TodoInput />

          {/* 选择不同的清单列表状态 */}
          <TodoMenu todos={todos} setfilteredTodos={setfilteredTodos} />

          {/* 每个清单 */}
          <ul>
            {filteredTodos.map((todo) => (
              <TodoItem key={todo.id} todo={todo} />
            ))}
          </ul>

          {/* 撤回和恢复 */}
          <TodoUndoRedo></TodoUndoRedo>
        </div>
      </div>
    </>
  );
}

export default TodoList;
