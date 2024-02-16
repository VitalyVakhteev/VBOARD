import React, { useState, useEffect } from 'react';
import axios from 'axios';

import MessageComponent from './MessageComponent';

const TopicModal = ({ isOpen, onClose, onSubmit, topic, onInputChange }) => {
    if (!isOpen) return null;

    const inputStyle = {
        display: 'block',
        marginBottom: '10px',
        width: '100%',
        boxSizing: 'border-box',
    };

    return (
        <div style={{ position: "fixed", top: 0, left: 0, right: 0, bottom: 0, backgroundColor: "rgba(0,0,0,0.5)", display: "flex", justifyContent: "center", alignItems: "center" }}>
            <div style={{ backgroundColor: "white", padding: 20, borderRadius: 5, width: '500px' }}>
                <h2>Add New Topic</h2>
                <form onSubmit={onSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    <input
                        type="text"
                        name="author"
                        value={topic.author}
                        onChange={onInputChange}
                        placeholder="Author"
                        required
                        style={inputStyle}
                    />
                    <input
                        type="text"
                        name="subject"
                        value={topic.subject}
                        onChange={onInputChange}
                        placeholder="Subject"
                        required
                        style={inputStyle}
                    />
                    <textarea
                        name="body"
                        value={topic.body}
                        onChange={onInputChange}
                        placeholder="Body"
                        required
                        style={{ ...inputStyle, height: '100px' }}
                    />
                    <div style={{ display: 'flex', justifyContent: 'flex-start', gap: '10px' }}>
                        <button type="submit" style={{ display: 'inline-block' }}>Submit</button>
                        <button type="button" onClick={onClose} style={{ display: 'inline-block' }}>Cancel</button>
                    </div>
                </form>
            </div>
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
        const isReply = message.subject.startsWith("Re:"); // TODO: Find better condition
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

const MessagePage = ({ setIsLoggedIn }) => {
    const [messages, setMessages] = useState([]);
    const [modalOpen, setModalOpen] = useState(false);
    const toggleModal = () => setModalOpen(!modalOpen);
    const [newTopic, setNewTopic] = useState({
        author: '',
        subject: '',
        body: ''
    });

    const fetchMessages = async () => {
        const response = await axios.get('http://localhost:8080/api/messages');
        const preprocessedMessages = preprocessMessages(response.data);
        setMessages(preprocessedMessages);
    };
    useEffect(() => {
        fetchMessages();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewTopic(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
    const handleLogout = () => {
        localStorage.removeItem('isLoggedIn');
        setIsLoggedIn(false);
    }
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('http://localhost:8080/api/messages/topic', newTopic);
            setNewTopic({ author: '', subject: '', body: '' });
            toggleModal();
            fetchMessages();
        } catch (error) {
            console.error("Error adding new topic:", error);
        }
    };

    return (
        <div>
            <h2>Home</h2>
            <button style={{float: "right"}} onClick={handleLogout}>Logout</button>
            <button style={{float: "right"}} onClick={toggleModal}>Add Topic</button>
            <TopicModal isOpen={modalOpen} onClose={toggleModal} onSubmit={handleSubmit} topic={newTopic}
                        onInputChange={handleInputChange}/>
            {messages.map((message) => (
                <MessageComponent key={message.id} message={message}/>
            ))}
        </div>
    );
};

export default MessagePage;