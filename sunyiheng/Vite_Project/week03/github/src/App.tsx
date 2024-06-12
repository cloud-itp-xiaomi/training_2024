import React, { Component } from "react";
import Search from "./components/Search";
import List from "./components/List";

export default class App extends Component {
  state = {
    users: [],
    isFirst: true, // 第一次打开页面
    isLoading: false, // 标识是否处于加载中
    err: "", // 存储请求相关的错误信息
  };

  // 状态在哪，操作状态的函数就在哪
  // 父组件给子组件传递函数，接收其传过来的数据
  updateAppState = (stateObj) => {
    this.setState(stateObj);
  };

  render() {
    return (
      <div className="container">
        <Search updateAppState={this.updateAppState} />
        <List {...this.state} />
      </div>
    );
  }
}
