// eslint-disable-next-line no-unused-vars
import React from "react";
import FilterLink from "../containers/FilterLink";

// 定义 Footer 组件，提供待办事项的过滤选项
const Footer = () => (
  <p>
    Show:
    {/* 显示全部待办事项的过滤链接 */}
    <FilterLink filter="SHOW_ALL">All</FilterLink>
    {", "}
    {/* 显示未完成待办事项的过滤链接 */}
    <FilterLink filter="SHOW_ACTIVE">Active</FilterLink>
    {", "}
    {/* 显示已完成待办事项的过滤链接 */}
    <FilterLink filter="SHOW_COMPLETED">Completed</FilterLink>
  </p>
);

// 导出 Footer 组件
export default Footer;
