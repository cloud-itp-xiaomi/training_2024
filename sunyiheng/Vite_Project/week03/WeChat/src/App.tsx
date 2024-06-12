import React, { Component } from "react";
import Header from "./components/Header";
import Content from "./components/Content";
import Footer from "./components/Footer";
import Fullscreen from "./components/Fullscreen";
import "./App.css";

export default class App extends Component {
  state = {
    isMinimize: false,
    isMaxmize: false,
    isClose: false,
  };

  fullscreenRef = React.createRef();

  changeStatus = (stateObj) => {
    this.setState(stateObj);
  };

  toggleFullScreen = () => {
    const { isMaxmize } = this.state;
    isMaxmize ? this.exitFullscreen() : this.enterFullscreen();
    // this.setState({ isMaxmize: !isMaxmize });
  };

  toggleMax = () => {
    const { isMinimize } = this.state;
    this.setState({ isMinimize: !isMinimize });
  };

  toggleOpen = () => {
    const { isClose } = this.state;
    this.setState({ isClose: !isClose });
  };

  enterFullscreen = () => {
    const element = this.fullscreenRef.current;
    element
      .requestFullscreen()
      .catch((err) => console.error("Error entering fullscreen:", err));
  };

  exitFullscreen = () => {
    document
      .exitFullscreen()
      .catch((err) => console.error("Error exiting fullscreen:", err));
  };

  render() {
    const stateObj = this.state;
    return (
      <>
        {stateObj.isMinimize ? (
          <h2 onClick={this.toggleMax}>您缩小了页面，点击返回页面</h2>
        ) : stateObj.isClose ? (
          <h2 onClick={this.toggleOpen}>您关闭了页面，点击返回页面</h2>
        ) : (
          <div
            ref={this.fullscreenRef}
            className={stateObj.isMaxmize ? "fullscreen-content" : ""}
          >
            <Header
              stateObj={stateObj}
              changeStatus={this.changeStatus}
              toggleFullScreen={this.toggleFullScreen}
            />
            <hr />
            <Content />
            <hr />
            <Footer />
          </div>
        )}
      </>
    );
  }
}
