// eslint-disable-next-line no-unused-vars
import React, { useState } from 'react';
import { FrownOutlined, FolderOpenOutlined, ScissorOutlined, MessageOutlined, VideoCameraOutlined } from '@ant-design/icons';

// eslint-disable-next-line react/prop-types
const Input = ({ onSend, owner }) => {
    const [text, setText] = useState('');

    const handleSend = () => {
        onSend(text, owner);
        setText('');
    };

    return (
    <div className='input'>
        <div className='inputicons'>
            <div className='inputiconsleft'>
                <FrownOutlined />
                <FolderOpenOutlined />
                <ScissorOutlined />
                <MessageOutlined />
            </div>
            <div className='inputiconsright'>
                <VideoCameraOutlined />
            </div>
        </div>
        <input
            type='text'
            value={text}
            onChange={(e) => setText(e.target.value)}
            onKeyDown={(e) => {
            if (e.key === 'Enter') {
                handleSend();
            }
            }}
        />
        <div className='send'>
            <button onClick={handleSend}>发送(S)</button>
        </div>
        </div>
    );
};

export default Input;
