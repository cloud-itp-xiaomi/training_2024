import { connect } from "react-redux";
import { setVisibilityFilter } from "../actions";
import Link from "../components/Link";

// 定义 mapStateToProps 函数，映射 state 到组件的 props
const mapStateToProps = (state, ownProps) => ({
  active: ownProps.filter === state.visibilityFilter, // 检查过滤器是否激活
});

// 定义 mapDispatchToProps 函数，映射 dispatch 到组件的 props
const mapDispatchToProps = (dispatch, ownProps) => ({
  onClick: () => {
    dispatch(setVisibilityFilter(ownProps.filter)); // 分发 setVisibilityFilter 动作
  },
});

// 将 Link 组件连接到 Redux store
const FilterLink = connect(mapStateToProps, mapDispatchToProps)(Link);

// 导出 FilterLink 容器组件
export default FilterLink;
