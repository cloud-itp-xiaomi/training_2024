import React, { Component } from "react";
import { Avatar, message } from "antd";
import PubSub from "pubsub-js";

export default class Item extends Component {
  // 模拟用户对话数据
  // state = {
  //   fontSize: "",
  //   userInfo: [
  //     {
  //       id: "01",
  //       userName: "user1",
  //       // userMessage: ["真垃圾", "给我一种武侠片的既视感"],
  //       userMessage: [
  //         {
  //           detail: "真垃圾",
  //           timeStamp: "2024-06-09T23:04:00",
  //         },
  //         {
  //           detail: "给我一种武侠片的既视感",
  //           timeStamp: "2024-06-09T23:05:00",
  //         },
  //       ],
  //       userImg: "user1.jpg",
  //     },
  //     {
  //       id: "02",
  //       userName: "user2",
  //       // userMessage: ["看封面就不想看", "这是鬼吹灯题材？"],
  //       userMessage: [
  //         { detail: "看封面就不想看", timeStamp: "2024-06-09T23:15:00" },
  //         {
  //           detail: "给我一种武侠片的既视感",
  //           timeStamp: "2024-06-09T23:18:00",
  //         },
  //       ],
  //       userImg: "monster.jpg",
  //       isMainUser: true,
  //     },
  //     {
  //       id: "03",
  //       userName: "user3",
  //       // userMessage: ["香港漫画改编", "算是黑帮混点武侠的类型"],
  //       userMessage: [
  //         { detail: "香港漫画改编", timeStamp: "2024-06-09T23:30:00" },
  //         {
  //           detail: "算是黑帮混点武侠的类型",
  //           timeStamp: "2024-06-09T23:30:20",
  //         },
  //       ],
  //       userImg: "user2.jpg",
  //     },
  //   ],
  // };

  state = {
    fontSize: "",
    userMessages: [
      {
        id: "01",
        userName: "user1",
        content: "真垃圾",
        userImg: "user1.jpg",
        isMainUser: false,
        timestamp: "2023-06-10T23:04:00",
      },
      {
        id: "02",
        userName: "user1",
        content: "给我一种武侠片的既视感",
        userImg: "user1.jpg",
        isMainUser: false,
        timestamp: "2023-06-10T23:10:00",
      },
      {
        id: "03",
        userName: "user2",
        content: "看封面就不想看",
        userImg: "monster.jpg",
        isMainUser: true,
        timestamp: "2023-06-10T23:12:00",
      },
      {
        id: "04",
        userName: "user2",
        content: "这是鬼吹灯题材？",
        userImg: "monster.jpg",
        isMainUser: true,
        timestamp: "2023-06-10T23:15:00",
      },
      {
        id: "05",
        userName: "user3",
        content: "香港漫画改编",
        userImg: "user2.jpg",
        isMainUser: false,
        timestamp: "2023-06-10T23:30:00",
      },
      {
        id: "06",
        userName: "user3",
        content: "算是黑帮混点武侠的类型",
        userImg: "user2.jpg",
        isMainUser: false,
        timestamp: "2023-06-10T23:35:00",
      },
    ],
  };

  elementRef = React.createRef();

  componentDidMount(): void {
    // 根据字数的多少来决定消息框的宽度
    // getComputedStyle是异步的，因此在执行此操作时，元素必须已经在DOM中
    if (this.elementRef.current) {
      const element = this.elementRef.current;
      const computedStyle = window.getComputedStyle(element);
      const fontSize = parseInt(computedStyle.fontSize, 10);
      this.setState({ fontSize });
    }

    this.token = PubSub.subscribe("updateMessage", (msg, data) => {
      const { userName, content, userImg, isMainUser } = data;
      const timestamp = new Date().toISOString();
      this.setState((prevState) => ({
        userMessages: [
          ...prevState.userMessages,
          {
            id: Date.now().toString(),
            userName,
            content,
            userImg:
              userName === "user1"
                ? "user1.jpg"
                : userName === "user2"
                ? "monster.jpg"
                : userName === "user3"
                ? "user3.jpg"
                : "",
            isMainUser: userName === "user2" ? true : false,
            timestamp,
          },
        ].sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp)),
      }));
    });
  }

  componentWillUnmount() {
    if (this.token) {
      PubSub.unsubscribe(this.token);
    }
  }

  render() {
    const { userMessages, fontSize } = this.state;

    return (
      <>
        {/* {userInfo.map((userObj) => {
          return (
            <div className="contentAll" key={userObj.id}>
              {userObj.userMessage.map((messageObj, index) => {
                const messageWidth = `${messageObj.detail.length * fontSize}px`;
                return (
                  <div
                    key={index}
                    className={userObj.isMainUser ? "mainUser" : "otherUser"}
                  >
                    <div className="avatar">
                      <Avatar
                        className="userIcon"
                        src={userObj.userImg}
                        shape="square"
                      />
                    </div>
                    <div className="userName">
                      {userObj.isMainUser ? "" : userObj.userName}
                    </div>
                    <div
                      ref={this.elementRef}
                      className="userMessage"
                      style={{
                        "--message-width": messageWidth,
                      }}
                    >
                      {messageObj.detail}
                    </div>
                  </div>
                );
              })}
            </div>
          );
        })} */}
        {userMessages.map((messageObj) => {
          const messageWidth = `${messageObj.content.length * fontSize}px`;
          return (
            <div className="contentAll" key={messageObj.id}>
              <div className={messageObj.isMainUser ? "mainUser" : "otherUser"}>
                <div className="avatar">
                  <Avatar
                    className="userIcon"
                    src={messageObj.userImg}
                    shape="square"
                  />
                </div>
                <div className="userName">
                  {messageObj.isMainUser ? "" : messageObj.userName}
                </div>
                <div
                  ref={this.elementRef}
                  className="userMessage"
                  style={{
                    "--message-width": messageWidth,
                  }}
                >
                  {messageObj.content}
                </div>
              </div>
            </div>
          );
        })}
      </>
    );
  }
}
