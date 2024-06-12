// src/components/ChatList.js
import React from 'react';
import styled from 'styled-components';

const ChatListContainer = styled.div`
  width: 30%;
  border-right: 1px solid #ccc;
  overflow-y: auto;
  background: #f9f9f9;
`;

const ChatItem = styled.div`
  padding: 15px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
  background: ${(props) => (props.selected ? '#e9ecef' : 'transparent')};

  &:hover {
    background: #e9ecef;
  }
`;

const ChatList = ({ chats, onSelectChat, selectedChat }) => {
  return (
    <ChatListContainer>
      {chats.map((chat) => (
        <ChatItem
          key={chat.id}
          onClick={() => onSelectChat(chat)}
          selected={selectedChat && selectedChat.id === chat.id}
        >
          {chat.name}
        </ChatItem>
      ))}
    </ChatListContainer>
  );
};

export default ChatList;
