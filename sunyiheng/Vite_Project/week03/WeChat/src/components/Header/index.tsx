import React, { Component, Fragment } from "react";
import {
  PushpinOutlined,
  LineOutlined,
  BorderOutlined,
  CloseOutlined,
  EllipsisOutlined,
} from "@ant-design/icons";
import "./index.css";

export default class Header extends Component {
  handlePin = () => {
    const { pin } = this;
    pin.classList.contains("pin")
      ? pin.classList.remove("pin")
      : pin.classList.add("pin");
  };

  handleMinimize = () => {
    const { stateObj, changeStatus } = this.props;
    changeStatus({
      isMinimize: !stateObj.isMinimize,
    });
  };

  handleMaxmize = () => {
    const { stateObj, changeStatus, toggleFullScreen } = this.props;
    changeStatus({ isMaximize: !stateObj.isMaximize });
    toggleFullScreen();
  };

  handleClose = () => {
    const { stateObj, changeStatus } = this.props;
    changeStatus({
      isClose: !stateObj.isClose,
    });
  };

  handleEllipsisDown = () => {
    const { ellips } = this;
    ellips.classList.add("ellips");
  };

  handleEllipsisUp = () => {
    const { ellips } = this;
    ellips.classList.remove("ellips");
  };

  render() {
    return (
      <div className="header">
        <h2>人大常委群(3)</h2>
        <div className="right">
          <div className="rightUp">
            <PushpinOutlined
              className="icon"
              onClick={this.handlePin}
              ref={(c) => (this.pin = c)}
            />
            <LineOutlined className="icon" onClick={this.handleMinimize} />
            <BorderOutlined className="icon" onClick={this.handleMaxmize} />
            <CloseOutlined className="icon" onClick={this.handleClose} />
          </div>
          <div className="rightBottom">
            <EllipsisOutlined
              onMouseDown={this.handleEllipsisDown}
              onMouseUp={this.handleEllipsisUp}
              ref={(c) => (this.ellips = c)}
            />
          </div>
        </div>
      </div>
    );
  }
}
