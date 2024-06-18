import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { add, toggle } from "../store/modules/todoListStore";
import { v4 as uuidv4 } from "uuid";

function TodoList() {
  // 设置输入框内容的状态
  const [input, setInput] = useState("");
  // useSelector()用来加载state中的数据
  const todos = useSelector((state) => state.todoList.todos);
  const dispatch = useDispatch();
  // 默认显示全部清单
  const [menu, setMenu] = useState("all");

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
      setInput("");
    }
  };

  // 切换完成状态
  const handleToggleTodo = (id) => {
    dispatch(toggle(id));
  };

  // 切换不同状态的清单列表
  const handleMenuTodo = () => {
    if (menu === "uncompleted") return todos.filter((todo) => !todo.complete);
    else if (menu === "completed") return todos.filter((todo) => todo.complete);
    else return todos;
  };
  const filteredTodos = handleMenuTodo();

  return (
    <>
      <input
        type="text"
        value={input}
        // 改变的时候更新数值
        onChange={(e) => setInput(e.target.value)}
        // 按下enter也可以增加清单
        onKeyDown={(e) => e.key === "Enter" && handleAddTodo()}
      />
      <button onClick={handleAddTodo}>新增清单</button>

      {/* 新增三个按钮可以实现切换列表 */}
      <br></br>
      <button onClick={() => setMenu("all")}>全部</button>
      <button onClick={() => setMenu("completed")}>已完成</button>
      <button onClick={() => setMenu("uncompleted")}>未完成</button>

      <ul>
        {filteredTodos.map((todo) => (
          <li key={todo.id} style={{ listStyle: "none" }}>
            {/* 复选框 */}
            <input
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
        ))}
      </ul>
    </>
  );
}

export default TodoList;
