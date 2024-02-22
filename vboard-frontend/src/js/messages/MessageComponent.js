import React, { useState } from 'react';

const MessageComponent = ({ message, level = 0 }) => {
    const [isImageClicked, setIsImageClicked] = useState(false);

    const messageStyle = {
        border: '1px solid #ddd',
        padding: '10px',
        marginTop: '10px',
        marginLeft: `${level * 20}px`,
    };

    const contentStyle = {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'flex-start',
        gap: '10px',
    };

    const imageStyle = {
        width: isImageClicked ? 'auto' : '100px',
        height: isImageClicked ? 'auto' : '100px',
        objectFit: 'cover',
        cursor: 'pointer',
    };

    const textStyle = {
        display: 'flex',
        flexDirection: 'column',
        textAlign: 'left',
    };

    const toggleImageSize = () => setIsImageClicked(!isImageClicked);

    return (
        <div style={messageStyle}>
            <div style={contentStyle}>
                {message.imageUrl && (
                    <img
                        src={message.imageUrl}
                        alt="Attached"
                        onClick={toggleImageSize}
                        style={imageStyle}
                    />
                )}
                <div style={textStyle}>
                    <p style={{ fontWeight: 'bold' }}>{message.author} | #{message.id} | {new Date(message.timestamp).toLocaleString()}</p>
                    <h4>{message.subject}</h4>
                    <p>{message.body}</p>
                </div>
            </div>
            {message.replies && message.replies.map(reply => (
                <MessageComponent key={reply.id} message={reply} level={level + 1} />
            ))}
        </div>
    );
};

export default MessageComponent;
