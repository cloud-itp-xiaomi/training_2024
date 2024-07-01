// 清单输入框

import { useState } from "react";
import { useDispatch } from "react-redux";
import { add } from "../store/modules/todoListStore";
import { v4 as uuidv4 } from "uuid";
import classes from "./TodoInput.module.css";

function TodoInput() {
  // 设置输入框内容的状态
  const [input, setInput] = useState("");
  const dispatch = useDispatch();

  // 新增数据
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

  return (
    <>
      {/* 输入框 */}
      <input
        className={classes.listinput}
        type="text"
        value={input}
        // 改变的时候更新数值
        onChange={(e) => setInput(e.target.value)}
        // 按下enter也可以增加清单
        onKeyDown={(e) => e.key === "Enter" && handleAddTodo()}
      />
      <button className={classes.add} onClick={handleAddTodo}>
        添加
      </button>
    </>
  );
}

export default TodoInput;
