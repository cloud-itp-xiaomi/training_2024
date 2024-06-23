// eslint-disable-next-line no-unused-vars
import React from "react";
import { ActionCreators as UndoActionCreators } from "redux-undo";
import { connect } from "react-redux";

// 定义 UndoRedo 容器组件，提供撤销和重做功能
// eslint-disable-next-line react/prop-types
let UndoRedo = ({ canUndo, canRedo, onUndo, onRedo }) => (
  <p>
    <button onClick={onUndo} disabled={!canUndo}>
      Undo
    </button>
    <button onClick={onRedo} disabled={!canRedo}>
      Redo
    </button>
  </p>
);

// 定义 mapStateToProps 函数，映射 state 到组件的 props
const mapStateToProps = (state) => ({
  canUndo: state.todos.past.length > 0, // 检查是否可以撤销
  canRedo: state.todos.future.length > 0, // 检查是否可以重做
});

// 定义 mapDispatchToProps 对象，映射 dispatch 到组件的 props
const mapDispatchToProps = {
  onUndo: UndoActionCreators.undo, // 映射撤销动作
  onRedo: UndoActionCreators.redo, // 映射重做动作
};

// 将 UndoRedo 组件连接到 Redux store
UndoRedo = connect(mapStateToProps, mapDispatchToProps)(UndoRedo);

// 导出 UndoRedo 容器组件
export default UndoRedo;
