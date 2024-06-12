import React, { Component } from "react";
import axios from "axios";

export default class Search extends Component {
  search = () => {
    // 获取用户输入，给 button 绑定事件，但是要获取 input 的值，ref/受控组件
    // 连续解构赋值+重命名
    const {
      keyWordElement: { value: keyWord },
    } = this;
    // 发送请求前通知 App 更新状态
    this.props.updateAppState({ isFirst: false, isLoading: true });
    // 发送网络请求，github 后端利用 cors 解决了跨域问题，所以不会产生跨域问题
    // Promise 风格，可以写 Fetch
    axios.get(`http://localhost:5173/api1/search/users?q=${keyWord}`).then(
      (response) => {
        // Search 组件搜索的结果要传给 List 组件展示，涉及到兄弟节点传递数据，要借助父组件 App
        // 请求成功后通知 App 更新状态
        this.props.updateAppState({
          isLoading: false,
          users: response.data.items,
        });
      },
      (error) => {
        // 请求失败更新状态
        this.props.updateAppState({ isLoading: false, err: error.message });
      }
    );
  };
  render() {
    return (
      <section className="jumbotron">
        <h3 className="jumbotron-heading">搜索 GitHub 用户</h3>
        <div>
          <input
            ref={(c) => (this.keyWordElement = c)}
            type="text"
            placeholder="请输入要搜索的用户昵称"
          />
          &nbsp;<button onClick={this.search}>搜索</button>
        </div>
      </section>
    );
  }
}
