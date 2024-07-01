// 右侧聊天区域

import Message from "./Message";
import { judgeTime } from "../utils/timeUtils";
import Icon from "./Icon";

function ChatArea({ currentChat, sendMessage, input, setInput }) {
  return (
    <div className="chat-area">
      {/* 群聊区域顶部 */}
      <div className="chat-header">
        <div>{currentChat.name}</div>
        <Icon iconHref="#icon-gengduo" title="聊天信息" className="icon-3" />
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
            <Icon iconHref="#icon-biaoqing" title="表情" className="icon-2" />
            <Icon
              iconHref="#icon-wenjian2"
              title="发送文件"
              className="icon-2"
            />
            <Icon iconHref="#icon-jietu" title="截图" className="icon-2" />
            <Icon
              iconHref="#icon-liaotianjilu"
              title="聊天记录"
              className="icon-2"
            />
          </div>
          <div className="input-right">
            <Icon
              iconHref="#icon-shipindianhua"
              title="语音聊天"
              className="icon-2"
            />
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
