import React from "react";
import { connect } from "react-redux";
import { useRoutes } from "react-router-dom";
import { toggle_todo, delete_todo } from "../../redux/actions";
import createRoutes from "../../routes";
import MyNavLink from "../../components/MyNavLink";
import Empty from "../../components/Empty";

function Content({ todos, toggle_todo, delete_todo }) {
  const handleChecked = (id) => {
    toggle_todo(id);
  };

  const handleDelete = (id) => {
    if (window.confirm("确认删除吗？")) {
      delete_todo(id);
    }
  };

  const present = todos.present;
  const allTodos = present;
  const completedTodos = present.filter((todo) => todo.completed);
  const activeTodos = present.filter((todo) => !todo.completed);

  const routes = useRoutes(
    createRoutes({
      allTodos,
      completedTodos,
      activeTodos,
      handleChecked,
      handleDelete,
    })
  );

  return (
    <>
      {present.length > 0 ? (
        <div style={{ margin: 10 }}>
          {/* todo: 这里可以用路由实现 */}
          <MyNavLink to="/all">全部</MyNavLink>
          &nbsp;
          <MyNavLink to="/completed">已完成</MyNavLink>
          &nbsp;
          <MyNavLink to="/active">进行中</MyNavLink>
          {routes}
        </div>
      ) : (
        <Empty />
      )}
    </>
  );
}

export default connect(
  (state) => ({
    todos: state.todos,
  }),
  { toggle_todo, delete_todo }
)(Content);
