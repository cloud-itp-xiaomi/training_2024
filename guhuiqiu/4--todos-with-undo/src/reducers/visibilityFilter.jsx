// 定义 visibilityFilter reducer，用于控制待办事项的可见性过滤器
const visibilityFilter = (state = "SHOW_ALL", action) => {
  switch (action.type) {
    case "SET_VISIBILITY_FILTER":
      // 处理设置可见性过滤器的动作，返回新的过滤器状态
      return action.filter;
    default:
      // 默认返回当前状态
      return state;
  }
};

// 导出 visibilityFilter reducer
export default visibilityFilter;
