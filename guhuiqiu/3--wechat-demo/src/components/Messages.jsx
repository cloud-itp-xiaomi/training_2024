/* eslint-disable react/prop-types */
// eslint-disable-next-line no-unused-vars
import React from 'react';
import Message from './Message';

// eslint-disable-next-line react/prop-types
const Messages = ({ messages }) => {
    return (
        <div className='Messages'>
        {messages.map((message, index) => (
            <Message key={index} message={message} />
        ))}
        </div>
    );
};

export default Messages;