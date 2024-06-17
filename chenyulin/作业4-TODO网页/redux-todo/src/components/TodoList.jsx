import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { add, toggle } from "../store/modules/todoListStore";
import { v4 as uuidv4 } from "uuid";

function TodoList() {
  // 设置输入框内容的状态
  const [input, setinput] = useState("");
  // useSelector()用来加载state中的数据
  const todos = useSelector((state) => state.todoList.todos);
  const dispatch = useDispatch();

  // 新增
  const handleAddTodo = () => {
    // 检查输入是否为空
    if (input.trim()) {
      dispatch(
        add({
          id: uuidv4(),
          text: input,
          // 默认未完成
          complete: false,
        })
      );
      // 清空输入框
      setinput("");
    }
  };

  // 切换完成状态
  const handleToggleTodo = (id) => {
    dispatch(toggle(id));
  };

  return (
    <>
      <input
        type="text"
        value={input}
        // 改变的时候更新数值
        onChange={(e) => setinput(e.target.value)}
        // 按下enter也可以增加清单
        onKeyDown={(e) => e.key === "Enter" && handleAddTodo()}
      />
      <button onClick={handleAddTodo}>新增清单</button>
      <ul>
        {todos.map((todo) => (
          <li
            key={todo.id}
            onClick={() => handleToggleTodo(todo.id)}
            style={{ textDecoration: todo.complete ? "line-through" : "none" }}
          >
            {todo.text}
          </li>
        ))}
      </ul>
    </>
  );
}

export default TodoList;
