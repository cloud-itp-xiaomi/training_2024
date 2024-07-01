// 展示不同状态的清单列表

import React, { useEffect, useState } from "react";
import classes from "./TodoMenu.module.css";

function TodoMenu({ todos, setfilteredTodos }) {
  const [menu, setMenu] = useState("all");

  // 切换不同状态的清单列表
  const handleMenuTodo = () => {
    if (menu === "uncompleted") return todos.filter((todo) => !todo.complete);
    else if (menu === "completed") return todos.filter((todo) => todo.complete);
    else return todos;
  };

  // 当菜单和列表中的信息发生改变时会重新处理
  useEffect(() => {
    setfilteredTodos(handleMenuTodo());
  }, [menu, todos]);

  return (
    <div className={classes.menu}>
      <button className={classes.menuitem} onClick={() => setMenu("all")}>
        全部
      </button>
      <button className={classes.menuitem} onClick={() => setMenu("completed")}>
        已完成
      </button>
      <button
        className={classes.menuitem}
        onClick={() => setMenu("uncompleted")}
      >
        未完成
      </button>
    </div>
  );
}

export default TodoMenu;
