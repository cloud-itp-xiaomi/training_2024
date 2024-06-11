/* eslint-disable react/prop-types */
// 右侧聊天区域

import Message from "./Message";

function ChatArea({ currentChat, sendMessage, input, setInput }) {
  //将时间转换为总分钟
  const timeToMinutes = (times) => {
    const [hours, minutes, seconds] = times.split(":").map(Number);
    return hours * 3600 + minutes * 60 + seconds;
  };

  // 判断相隔时间超没超过两分钟
  const judgeTime = (current, prev) => {
    if (!prev) return true;
    const diff =
      timeToMinutes(current.timestamp) - timeToMinutes(prev.timestamp);
    if (diff > 120) return true;
  };

  return (
    <div className="chat-area">
      {/* 群聊区域顶部 */}
      <div className="chat-header">
        <div>{currentChat.name}</div>
        <svg className="icon-3" aria-hidden="true">
          <title>聊天信息</title>
          <use xlinkHref="#icon-gengduo"></use>
        </svg>
      </div>
      {/* 聊天内容区域 */}
      <div className="message-area">
        {/* 遍历当前聊天内容的数据 */}
        {currentChat.messages.map((message, index) => {
          // 判断是否有聊天记录
          const preMessages =
            index > 0 ? currentChat.messages[index - 1] : null;
          // 判断连续对话有没有超过两分钟
          const showTime = judgeTime(message, preMessages);
          return (
            // 每条的具体消息
            <Message key={message.id} message={message} showTime={showTime} />
          );
        })}
      </div>

      {/* 输入区域 */}
      <div className="input-area">
        {/* 输入区域顶部的各种工具 */}
        <div className="input-top">
          <div className="input-left">
            <svg className="icon-2" aria-hidden="true">
              <title>表情</title>
              <use xlinkHref="#icon-biaoqing"></use>
            </svg>
            <svg className="icon-2" aria-hidden="true">
              <title>发送文件</title>
              <use xlinkHref="#icon-wenjian2"></use>
            </svg>
            <svg className="icon-2" aria-hidden="true">
              <title>截图</title>
              <use xlinkHref="#icon-jietu"></use>
            </svg>
            <svg className="icon-2" aria-hidden="true">
              <title>聊天记录</title>
              <use xlinkHref="#icon-liaotianjilu"></use>
            </svg>
          </div>
          <div className="input-right">
            <svg className="icon-2" aria-hidden="true">
              <title>语音聊天</title>
              <use xlinkHref="#icon-shipindianhua"></use>
            </svg>
          </div>
        </div>

        {/* 输入文本框 */}
        <textarea
          type="text"
          value={input}
          // 改变的时候更新数值
          onChange={(e) => setInput(e.target.value)}
          // 按下enter也可以发送消息
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        ></textarea>

        {/* 发送按钮 */}
        <button onClick={sendMessage}>发送(S)</button>
      </div>
    </div>
  );
}

export default ChatArea;
