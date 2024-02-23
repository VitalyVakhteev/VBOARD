import React, {useState, Fragment} from 'react';
import DOMPurify from 'dompurify';

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
    const handleMessageLinkClick = (id, event) => {
        event.preventDefault();
        const element = document.getElementById(`message-${id}`);
        if (element) {
            const topPosition = element.offsetTop;
            window.scrollTo({
                top: topPosition,
                behavior: 'smooth',
            });
        }
    };

    const parseMessageBody = (body) => {
        const cleanBody = DOMPurify.sanitize(body, {
            ALLOWED_TAGS: ['span', 'a', 'b', 'i', 'em', 'strong'],
            ALLOWED_ATTR: ['href', 'style', 'data-id', 'rel', 'target'],
        });

        const parts = cleanBody.split(/(#\d+)/g);
        return parts.map((part, index) => {
            if (part.match(/#\d+/)) {
                const id = part.substring(1);
                return (
                    <span key={index} style={{ color: 'blue', cursor: 'pointer', textDecoration: 'underline' }}
                          onClick={(e) => handleMessageLinkClick(id, e)}>
                        {part}
                    </span>
                );
            }
            if (part.includes('<a')) {
                const sanitizedPart = DOMPurify.sanitize(part, {
                    ADD_ATTR: ['target', 'rel'],
                });
                return <span key={index} dangerouslySetInnerHTML={{ __html: sanitizedPart }} />;
            }
            return <Fragment key={index}>{DOMPurify.sanitize(part)}</Fragment>;
        });
    };

    const toggleImageSize = () => setIsImageClicked(!isImageClicked);
    const messageBodyElements = parseMessageBody(message.body);

    return (
        <div style={messageStyle} id={`message-${message.id}`}>
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
                    <p>{messageBodyElements}</p>
                </div>
            </div>
            {message.replies && message.replies.map(reply => (
                <MessageComponent key={reply.id} message={reply} level={level + 1} />
            ))}
        </div>
    );
};

export default MessageComponent;
