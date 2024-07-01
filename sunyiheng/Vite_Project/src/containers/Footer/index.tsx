import React from "react";
import { connect } from "react-redux";
import { ActionCreators as UndoRedo } from "redux-undo";
import { Button } from "antd";
import { check_all, clear_completed } from "../../redux/actions";
import "./index.css";

function Footer({
  todos,
  check_all,
  clear_completed,
  onUndo,
  onRedo,
  canUndo,
  canRedo,
}) {
  const present = todos.present ? todos.present : [];
  const completedTodos = present.filter((todo) => todo.completed);

  const handleClear = () => {
    const newTodos = present.filter((todo) => !todo.completed);
    clear_completed(newTodos);
  };

  const handleCheckedAll = (completed) => {
    const newTodos = present.map((todo) => ({ ...todo, completed }));
    check_all(newTodos);
  };

  return present.length > 0 ? (
    <div>
      <div className="footerTop">
        <label>
          <input
            type="checkbox"
            checked={
              completedTodos.length === present.length && present.length !== 0
                ? true
                : false
            }
            onChange={(e) => handleCheckedAll(e.target.checked)}
          />
        </label>
        <span>
          <span>已完成{completedTodos.length}</span> / 全部{present.length}
        </span>
        &nbsp;
        <Button onClick={handleClear} className="clearCompletedBtn">
          清除已完成任务
        </Button>
      </div>
      <div className="footerBottom">
        <Button onClick={onUndo} disabled={!canUndo}>
          撤销
        </Button>
        &nbsp;
        <Button onClick={onRedo} disabled={!canRedo}>
          重做
        </Button>
      </div>
    </div>
  ) : (
    ""
  );
}

export default connect(
  (state) => ({
    todos: state.todos,
    // 通过当前栈的长度是否大于0来判断能否撤销和重做
    canUndo: state.todos.present.length > 0,
    canRedo: state.todos.future.length > 0,
  }),
  {
    check_all,
    clear_completed,
    // redux-undo 的 ActionCreators 方法
    onUndo: UndoRedo.undo,
    onRedo: UndoRedo.redo,
  }
)(Footer);
