import React, { Component } from "react";

const DetailData = [
  { id: "01", content: "你好，中国" },
  { id: "02", content: "你好，上海" },
  { id: "03", content: "你好，美国" },
];

export default class Detail extends Component {
  render() {
    // 接收params参数
    const { id, title } = this.props.match.params;

    // 接收search参数
    // const { search } = this.props.location;

    // 接收state参数
    // const {id, title} = this.props.location.state || {}
    const findResult =
      DetailData.find((detailObj) => {
        return detailObj.id === id;
      }) || {};
    return (
      <ul>
        <li>ID:{id}</li>
        <li>TITLE:{title}</li>
        <li>CONTENT:{findResult.content}</li>
      </ul>
    );
  }
}
