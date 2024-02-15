import React from 'react';

const MessageComponent = ({ message, level = 0 }) => {
    const indentationStyle = { marginLeft: `${level * 20}px`, textAlign: 'left' };

    return (
        <div style={indentationStyle}>
            <h4>{message.subject}</h4>
            <p>{message.body}</p>
            {message.childList && message.childList.map(child =>
                <MessageComponent key={child.id} message={child} level={level + 1} />
            //     TODO: FIX INDENTATION
            )}
        </div>
    );
};

export default MessageComponent;
