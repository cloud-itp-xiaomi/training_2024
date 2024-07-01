import React from "react";
import TodoList from "../components/TodoList";
import { Navigate } from "react-router-dom";

const createRoutes = ({
  allTodos,
  completedTodos,
  activeTodos,
  handleChecked,
  handleDelete,
}) => {
  const commonProps = { handleChecked, handleDelete };

  return [
    {
      path: "/all",
      element: <TodoList todos={allTodos} {...commonProps} />,
    },
    {
      path: "/completed",
      element: <TodoList todos={completedTodos} {...commonProps} />,
    },
    {
      path: "/active",
      element: <TodoList todos={activeTodos} {...commonProps} />,
    },
    {
      path: "/",
      element: <Navigate to="/all" />,
    },
  ];
};

export default createRoutes;
