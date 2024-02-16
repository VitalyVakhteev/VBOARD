import React from 'react';

const MessageComponent = ({ message, level = 0 }) => {
    const indentationStyle = {
        marginLeft: `${level * 20}px`,
        textAlign: 'left',
        border: '1px solid #ddd',
        padding: '10px',
        marginTop: '10px',
    };

    return (
        <div style={indentationStyle}>
            <p style={{ fontWeight: 'bold' }}>Author: {message.author} | ID: {message.id}</p>
            <h4>{message.subject}</h4>
            <p>{message.body}</p>
            {message.childList && message.childList.map(child =>
                <MessageComponent key={child.id} message={child} level={level + 1} />
            )}
        </div>
    );
};

export default MessageComponent;
