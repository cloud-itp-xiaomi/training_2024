// eslint-disable-next-line no-unused-vars
import React from "react";
import PropTypes from "prop-types";

// 定义 Link 组件，用于渲染过滤链接
const Link = ({ active, children, onClick }) => {
  if (active) {
    // 如果链接处于活动状态，渲染一个不可点击的 span
    return <span>{children}</span>;
  }

  // 否则渲染一个可点击的链接
  return (
    <a
      href="#"
      onClick={(e) => {
        // 阻止默认链接行为
        e.preventDefault();
        // 调用传递的点击处理函数
        onClick();
      }}
    >
      {children}
    </a>
  );
};

// 定义 Link 组件的属性类型
Link.propTypes = {
  active: PropTypes.bool.isRequired, // 链接是否处于活动状态
  children: PropTypes.node.isRequired, // 链接的子元素
  onClick: PropTypes.func.isRequired, // 链接的点击处理函数
};

// 导出 Link 组件
export default Link;
