import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Message from './Message';
import ChatInput from './ChatInput';

// 导入本地图片
import momo from '../assets/momo.jpg';
import meme from '../assets/meme.jpg';
import jack from '../assets/jack.jpg';
import rose from '../assets/rose.jpg';
import mom from '../assets/mom.jpg';
import dad from '../assets/dad.jpg';

const ChatBoxContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const MessagesContainer = styled.div`
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background: #f9f9f9;
`;

const InputsContainer = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 10px;
  border-top: 1px solid #ccc;
  background: #fff;
`;

const LeftInputContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: row;
`;

const RightInputContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: row-reverse;
`;

const ChatBox = ({ chat }) => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const storedMessages = localStorage.getItem(`messages-${chat.id}`);
    if (storedMessages) {
      setMessages(JSON.parse(storedMessages));
    } else {
      setMessages(chat.messages);
    }
  }, [chat]);

  useEffect(() => {
    localStorage.setItem(`messages-${chat.id}`, JSON.stringify(messages));
  }, [messages, chat.id]);

  const avatars = {
    'momo': momo,
    'meme': meme,
    'jack': jack,
    'rose': rose,
    '妈': mom,
    '爸': dad
  };

  const addMessage = (message, isUser1) => {
    const user = isUser1 ? chat.members[0] : chat.members[1];
    const newMessage = {
      text: message,
      isUser1,
      name: user,
      avatar: avatars[user],
    };
    setMessages([...messages, newMessage]);
  };

  return (
    <ChatBoxContainer>
      <MessagesContainer>
        {messages.map((message, index) => (
          <Message key={index} message={message} />
        ))}
      </MessagesContainer>
      <InputsContainer>
        <LeftInputContainer>
          <ChatInput onSendMessage={(msg) => addMessage(msg, true)} />
        </LeftInputContainer>
        <RightInputContainer>
          <ChatInput onSendMessage={(msg) => addMessage(msg, false)} />
        </RightInputContainer>
      </InputsContainer>
    </ChatBoxContainer>
  );
};

export default ChatBox;
