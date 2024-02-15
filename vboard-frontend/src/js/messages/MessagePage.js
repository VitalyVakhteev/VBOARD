import React, { useState, useEffect } from 'react';
import axios from 'axios';

import MessageComponent from './MessageComponent';

const MessagePage = () => {
    const [messages, setMessages] = useState([]);

    // const handleLogout = () => {
    //     localStorage.removeItem('isLoggedIn');
    //     window.location.href = '/login';
    // }
    // Dummy method for now

    useEffect(() => {
        const fetchMessages = async () => {
            const response = await axios.get('http://localhost:8080/api/messages');
            const preprocessedMessages = preprocessMessages(response.data);
            setMessages(preprocessedMessages);
        };
        fetchMessages();
    }, []);

    return (
        <div>
            <h2>Home</h2>
            {messages.map((message) => (
                <MessageComponent key={message.id} message={message} />
            ))}
        </div>
    );
};

const preprocessMessages = (messages) => {
    const messageMap = {};
    const topLevelMessages = [];

    messages.forEach(message => {
        messageMap[message.id] = { ...message, childList: [] };
    });

    messages.forEach(message => {
        const isReply = message.subject.startsWith("Re:");
        if (isReply) {
            const parentMessage = Object.values(messageMap).find(m => message.subject.includes(m.subject));
            if (parentMessage) {
                parentMessage.childList.push(messageMap[message.id]);
            }
        } else {
            topLevelMessages.push(messageMap[message.id]);
        }
    });

    return topLevelMessages;
};

export default MessagePage;