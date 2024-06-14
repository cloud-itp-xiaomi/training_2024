/* eslint-disable react/prop-types */
// eslint-disable-next-line no-unused-vars
import React from 'react';

// eslint-disable-next-line react/prop-types
const Message = ({ message }) => {
    const avatarUrl = message.owner
    ? 'https://images.unsplash.com/photo-1591160690555-5debfba289f0?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8cHVwcHl8ZW58MHx8MHx8fDA%3D' // 你的头像URL
    : 'https://images.unsplash.com/photo-1589254065909-b7086229d08c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cm9ib3R8ZW58MHx8MHx8fDA%3D'; // 机器人的头像URL
    return (
        <div className={`message ${message.owner ? 'owner' : ''}`}>
        <div className='messageInfo'>
            <span>{message.username}</span>
            <img src={avatarUrl} alt='User' />
        </div>
        <div className='messageContent'>
            <p>{message.text}</p>
        </div>
        </div>
    );
};

export default Message;