/* eslint-disable react/prop-types */
// 左侧聊天列表

function ChatList({ sortedChatGroups, setCurrentChatId, currentChatId }) {
  // 切换群聊
  const handleChatClick = (chatId) => {
    setCurrentChatId(chatId);
  };

  //格式化时间
  const formatTimestamp = (timestamp) => {
    return timestamp.substring(0, 5);
  };

  return (
    <div className="sidebar">
      {/* 顶部搜索栏 */}
      <div className="sidebar-top">
        {/* 搜索 */}
        <div className="search">
          <svg className="icon-4" aria-hidden="true">
            <use xlinkHref="#icon-search"></use>
          </svg>
          <input type="text" placeholder="搜索" />
        </div>
        {/* 创建群聊 */}
        <div className="creat">
          <svg className="icon-5" aria-hidden="true">
            <use xlinkHref="#icon-faqiqunliao"></use>
          </svg>
        </div>
      </div>

      {/* 各个群组 */}
      <div className="chat-list">
        {/* 遍历排序后的数据 */}
        {sortedChatGroups.map((chat) => (
          <div
            key={chat.id}
            className={`chat ${currentChatId === chat.id ? "active" : ""}`}
            onClick={() => handleChatClick(chat.id)}
          >
            {/* 群头像 */}
            <img src={chat.photo} alt="photo" className="group" />
            {/* 群聊信息 */}
            <div className="group-info">
              {/* 群聊的名称 */}
              <div className="group-name">{chat.name}</div>
              {/* 群聊的最新内容 */}
              <div className="group-message">
                {chat.messages.length > 0
                  ? `${chat.messages[chat.messages.length - 1].sender}: ${
                      chat.messages[chat.messages.length - 1].text
                    }`
                  : ""}
              </div>
              {/* 群聊的最新时间 */}
              <div className="group-time">
                {chat.messages.length > 0
                  ? formatTimestamp(
                      chat.messages[chat.messages.length - 1].timestamp
                    )
                  : ""}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ChatList;
