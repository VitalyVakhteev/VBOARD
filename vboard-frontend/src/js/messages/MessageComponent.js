import React from 'react';

const MessageComponent = ({ message, level = 0 }) => {
    const indentationStyle = {
        marginLeft: `${level * 20}px`,
        textAlign: 'left',
        border: '1px solid #ddd',
        padding: '10px',
        marginTop: '10px',
    };
    const formattedTimestamp = new Date(message.timestamp).toLocaleString();

    return (
        <div style={indentationStyle}>
            <p style={{ fontWeight: 'bold' }}>{message.author} | #{message.id} | {formattedTimestamp}</p>
            <h4>{message.subject}</h4>
            <p>{message.body}</p>
            {message.replies && message.replies.map(reply =>
                <MessageComponent key={reply.id} message={reply} level={level + 1} />
            )}
        </div>
    );
};

export default MessageComponent;
