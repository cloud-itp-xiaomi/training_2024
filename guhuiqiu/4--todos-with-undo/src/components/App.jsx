/* eslint-disable no-unused-vars */
import React from 'react';
import Footer from "./Footer";
import AddTodo from "../containers/AddTodo";
import VisibleTodoList from "../containers/VisibleTodoList";
import UndoRedo from "../containers/UndoRedo";

// 定义 App 组件，作为整个应用的根组件
const App = () => (
  <div>
    {/* 添加待办事项的输入框和按钮 */}
    <AddTodo />
    {/* 显示待办事项列表 */}
    <VisibleTodoList />
    {/* 显示过滤器选项 */}
    <Footer />
    {/* 显示撤销和重做按钮 */}
    <UndoRedo />
  </div>
);

// 导出 App 组件
export default App;
