/* eslint-disable react/prop-types */
// 每条的具体消息

function Message({ message, showTime }) {
  //格式化时间
  const formatTimestamp = (timestamp) => {
    return timestamp.substring(0, 5);
  };

  return (
    <div className={`message ${message.senderId === 0 ? "right" : "left"}`}>
      {/* 如果超过了两分钟才显示时间 */}
      {showTime && (
        <span className="timestamp">{formatTimestamp(message.timestamp)}</span>
      )}
      {/* 每一个聊天对话的内容 */}
      <div className="message-info">
        {/* 聊天用户的头像 */}
        <img src={message.photo} alt="photo" className="photo" />
        <div className="message-content">
          {message.senderId !== 0 && (
            // 用户的名字
            <span className="sender">{message.sender}</span>
          )}
          {/* 用户输入的内容 */}
          <span className="text">{message.text}</span>
        </div>
      </div>
    </div>
  );
}

export default Message;
