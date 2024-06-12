// src/components/Message.js
import React from 'react';
import styled from 'styled-components';

const MessageContainer = styled.div`
  display: flex;
  align-items: flex-start;
  justify-content: ${(props) => (props.isUser1 ? 'flex-end' : 'flex-start')};
  margin-bottom: 10px;
`;

const MessageBubble = styled.div`
  padding: 10px;
  background: ${(props) => (props.isUser1 ? '#d1e7dd' : '#f1f1f1')};
  border-radius: 10px;
  max-width: 60%;
`;

const UserInfo = styled.div`
  display: flex;
  align-items: center;
  margin-right: ${(props) => (props.isUser1 ? '0' : '5px')};
  margin-left: ${(props) => (props.isUser1 ? '5px' : '0')};
`;

const UserAvatar = styled.img`
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 5px;
`;

const UserName = styled.span`
  font-weight: bold;
  font-size: 12px;
  margin-right: 5px;
`;

const Message = ({ message }) => {
  return (
    <MessageContainer isUser1={message.isUser1}>
      {!message.isUser1 && (
        <UserInfo>
          <UserAvatar src={message.avatar} />
          <UserName>{message.name}</UserName>
        </UserInfo>
      )}
      <MessageBubble isUser1={message.isUser1}>{message.text}</MessageBubble>
      {message.isUser1 && (
        <UserInfo isUser1={message.isUser1}>
          <UserAvatar src={message.avatar}  />
          <UserName>{message.name}</UserName>
        </UserInfo>
      )}
    </MessageContainer>
  );
};

export default Message;
