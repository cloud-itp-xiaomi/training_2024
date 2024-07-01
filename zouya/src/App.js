// src/App.js
import React, { useState } from 'react';
import styled from 'styled-components';
import ChatList from './components/ChatList';
import ChatBox from './components/ChatBox';

const AppContainer = styled.div`
  display: flex;
  height: 100vh;
  width: 100%;
`;

const App = () => {
  const [selectedChat, setSelectedChat] = useState(null);

  const chats = [
    { id: 1, name: '对话列表1', members: ['momo', 'meme'], messages: [] },
    { id: 2, name: '对话列表2', members: ['jack', 'rose'], messages: [] },
    { id: 3, name: '对话列表3', members: ['妈', '爸'], messages: [] },
  ];

  return (
    <AppContainer>
      <ChatList chats={chats} onSelectChat={setSelectedChat} selectedChat={selectedChat} />
      {selectedChat ? (
        <ChatBox chat={selectedChat} key={selectedChat.id} />
      ) : (
        <div style={{ flex: 1, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          选择对话
        </div>
      )}
    </AppContainer>
  );
};

export default App;
