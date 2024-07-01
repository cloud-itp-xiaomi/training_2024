import React, { Component, Fragment } from "react";
import {
  SmileOutlined,
  FolderOutlined,
  ScissorOutlined,
  CommentOutlined,
  PhoneOutlined,
} from "@ant-design/icons";
import "./index.css";
import PubSub from "pubsub-js";

export default class Footer extends Component {
  state = { userInput: "" };

  handleChange = (e) => {
    this.setState({ userInput: e.target.value });
  };

  handleSubmit = (e) => {
    e.preventDefault();
    const { userInput } = this.state;
    if (userInput.trim()) {
      PubSub.publish("updateMessage", {
        userName: "user2",
        content: userInput,
        userImg: "monster.jpg",
      });
      this.setState({ userInput: "" });
    }
  };

  handleMouseDown = (e) => {
    const clickedIcon = e.target;
    const iconWrapper = clickedIcon.closest(".iconWrapper");
    iconWrapper ? iconWrapper.classList.add("ellips") : "";
  };

  handleMouseUp = (e) => {
    const clickedIcon = e.target;
    const iconWrapper = clickedIcon.closest(".iconWrapper");
    iconWrapper ? iconWrapper.classList.remove("ellips") : "";
  };

  render() {
    return (
      <div className="footer">
        <div
          className="footerTopLeft"
          ref={(c) => (this.icon = c)}
          onMouseDown={this.handleMouseDown}
          onMouseUp={this.handleMouseUp}
          onMouseLeave={this.handleMouseUp}
        >
          <SmileOutlined className="iconWrapper" />
          <FolderOutlined className="iconWrapper" />
          <ScissorOutlined className="iconWrapper" />
          <CommentOutlined className="iconWrapper" />
          <PhoneOutlined className="phone iconWrapper" />
        </div>
        <form className="submitForm" onSubmit={this.handleSubmit}>
          <input
            className="submitInput"
            type="text"
            value={this.state.userInput}
            onChange={this.handleChange}
          />
          <button className="submitButton" type="submit">
            发送
          </button>
        </form>
      </div>
    );
  }
}
