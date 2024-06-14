// eslint-disable-next-line no-unused-vars
import React, { useState } from 'react';
import { EllipsisOutlined } from '@ant-design/icons';
import Messages from './Messages';
import Input from './Input';

const Chat = () => {
    const [messages, setMessages] = useState([]);

    const handleSend = (text, owner) => {
        if (text.trim()) {
            const username = owner ? '我' : '机器人';
            setMessages([...messages, { text, owner ,username}]);
        }
    };

    return (
    <div className='chat'>
        <div className='chatInfo'>
            <span>前端实习小组</span>
            <EllipsisOutlined />
        </div>
        <Messages messages={messages} />
        <Input onSend={(text) => handleSend(text, true)} owner={true} />
        <Input onSend={(text) => handleSend(text, false)} owner={false} />
    </div>
  );
};

export default Chat;
