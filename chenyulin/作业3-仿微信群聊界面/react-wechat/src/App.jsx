import { useEffect, useState } from "react";
import "./App.css";
import { initialChatGroups } from "./data/chat";
import Navigation from "./components/Navigation";
import ChatList from "./components/ChatList";
import ChatArea from "./components/ChatArea";
import { v4 as uuidv4 } from "uuid";

function App() {
  // 聊天的列表和内容数据
  const [chatGroups, setChatGroups] = useState(initialChatGroups);
  // 排序后的聊天组数据
  const [sortedChatGroups, setSortedChatGroups] = useState([]);

  // 排序聊天数据并更新状态
  useEffect(() => {
    setSortedChatGroups(sortChatGroups([...chatGroups]));
  }, [chatGroups]);

  //当前群聊的id,默认是第一个群聊
  const [currentChatId, setCurrentChatId] = useState(chatGroups[0].id);
  // 当前群聊的内容
  const currentChat = chatGroups.find((chat) => chat.id === currentChatId);

  // 输入框输入的内容
  const [input, setInput] = useState("");

  // 按时间排序
  const sortChatGroups = (chatGroups) => {
    console.log("排序");
    return [...chatGroups].sort((a, b) => {
      // 获取最后一条消息的时间，如果没有消息，则返回空字符串
      const lastMessageA =
        a.messages.length > 0
          ? a.messages[a.messages.length - 1].timestamp
          : "";
      const lastMessageB =
        b.messages.length > 0
          ? b.messages[b.messages.length - 1].timestamp
          : "";
      // 如果时间相等，则不改变顺序
      if (lastMessageA === lastMessageB) return 0;
      // 如果A为空，则A排在后面
      if (lastMessageA === "") return 1;
      // 如果B为空，则B排在后面
      if (lastMessageB === "") return -1;
      // 如果都有时间，根据时间排序（从最新到最旧）
      return lastMessageB.localeCompare(lastMessageA);
    });
  };

  // 发送消息
  const sendMessage = () => {
    if (input.trim()) {
      const newMessage = {
        id: uuidv4(),
        text: input,
        timestamp: new Date().toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
        }),
        sender: "你",
        senderId: 0,
        photo: "/photo1.jpg",
      };

      // 更新聊天内容
      const updatedGroups = chatGroups.map((chat) => {
        if (chat.id === currentChatId) {
          return { ...chat, messages: [...chat.messages, newMessage] };
        }
        return chat;
      });
      setChatGroups(updatedGroups);

      //将输入框清空
      setInput("");
    }
  };

  return (
    <div className="app">
      {/* 黑色导航栏 */}
      <Navigation />

      {/* 聊天列表 */}
      <ChatList
        sortedChatGroups={sortedChatGroups}
        setCurrentChatId={setCurrentChatId}
        currentChatId={currentChatId}
      />

      {/* 聊天区域 */}
      <ChatArea
        currentChat={currentChat}
        sendMessage={sendMessage}
        input={input}
        setInput={setInput}
      />
    </div>
  );
}

export default App;
