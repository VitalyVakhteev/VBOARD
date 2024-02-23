import React, {useState, useEffect, useCallback} from 'react';
import axios from 'axios';
import MessageComponent from './MessageComponent';

const TopicModal = ({ isOpen, onClose, onSubmit, topic, onInputChange, setSelectedFile }) => {

    const handleFileChange = (e) => {
        if (e.target.files[0]) {
            setSelectedFile(e.target.files[0]);
        }
    };

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
                <form onSubmit={onSubmit} style={{display: 'flex', flexDirection: 'column', gap: '10px'}}>
                    <input
                        type="file"
                        onChange={handleFileChange}
                        style={{marginBottom: '10px'}}
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
                        style={{...inputStyle, height: '100px'}}
                    />
                    <div style={{display: 'flex', justifyContent: 'flex-start', gap: '10px'}}>
                        <button type="submit" style={{display: 'inline-block'}}>Submit</button>
                        <button type="button" onClick={onClose} style={{display: 'inline-block'}}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

const ReplyModal = ({isOpen, onClose, onSubmit, reply, onInputChange, parentId, setSelectedFile }) => {
    const handleFileChange = (e) => {
        if (e.target.files[0]) {
            setSelectedFile(e.target.files[0]);
        }
    };

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
                <h2>Add New Reply</h2>
                <form onSubmit={(e) => onSubmit(e, parentId)}
                      style={{display: 'flex', flexDirection: 'column', gap: '10px'}}>
                    <input
                        type="file"
                        onChange={handleFileChange}
                        style={{marginBottom: '10px'}}
                    />
                    <textarea
                        name="body"
                        value={reply.body}
                        onChange={onInputChange}
                        placeholder="Reply"
                        required
                        style={{...inputStyle, height: '100px'}}
                    />
                    <div style={{display: 'flex', justifyContent: 'flex-start', gap: '10px'}}>
                        <button type="submit" style={{display: 'inline-block'}}>Submit</button>
                        <button type="button" onClick={onClose} style={{display: 'inline-block'}}>Cancel</button>
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
    const REACT_APP_IMGUR_CLIENT_ID = process.env.REACT_APP_IMGUR_CLIENT_ID;
    const REACT_APP_API_HOST = process.env.REACT_APP_API_HOST;
    const [messages, setMessages] = useState([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [replyModalOpen, setReplyModalOpen] = useState(false);
    const [replyingToId, setReplyingToId] = useState(null);
    const [selectedFile, setSelectedFile] = useState(null);
    const toggleModal = () => setModalOpen(!modalOpen);
    const [newTopic, setNewTopic] = useState({
        subject: '',
        body: ''
    });
    const [newReply, setNewReply] = useState({
        body: ''
    });

    const fetchMessages = useCallback(async () => {
        const response = await axios.get(REACT_APP_API_HOST + '/api/messages');
        const preprocessedMessages = preprocessMessages(response.data);
        setMessages(preprocessedMessages);
    }, [REACT_APP_API_HOST]);
    useEffect(() => {
        fetchMessages();
    }, [fetchMessages]);

    const uploadImageToImgur = async (file) => {
        const formData = new FormData();
        formData.append('image', file);

        try {
            const response = await axios.post('https://api.imgur.com/3/image', formData, {
                headers: {
                    'Authorization': ('Client-ID ' + REACT_APP_IMGUR_CLIENT_ID),
                    'Content-Type': 'multipart/form-data',
                },
            });
            return response.data.data.link;
        } catch (error) {
            console.error('Failed to upload image to Imgur:', error);
            return '';
        }
    };


    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewTopic(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleReplyInputChange = (e) => {
        const { name, value } = e.target;
        setNewReply(prevState => ({
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
        let imageUrl = '';
        if (selectedFile) {
            imageUrl = await uploadImageToImgur(selectedFile);
            setSelectedFile(null);
        }
        const author = localStorage.getItem('currentUsername');
        const topicToSubmit = { ...newTopic, author, imageUrl, type: 'topic' };

        try {
            await axios.post(REACT_APP_API_HOST + '/api/messages/topic', topicToSubmit);
            setNewTopic({ subject: '', body: '' });
            toggleModal();
            fetchMessages();
        } catch (error) {
            console.error("Error adding new topic:", error);
        }
    };

    const handleReplySubmit = async (e, parentId) => {
        e.preventDefault();
        let imageUrl = '';
        if (selectedFile) {
            imageUrl = await uploadImageToImgur(selectedFile);
            setSelectedFile(null);
        }
        const author = localStorage.getItem('currentUsername');
        const replyToSubmit = { ...newReply, author, parentId, imageUrl, type: 'reply' };

        try {
            await axios.post(REACT_APP_API_HOST + `/api/messages/reply?parentId=${parentId}`, replyToSubmit);
            setNewReply({ body: '' });
            setReplyModalOpen(false);
            fetchMessages();
        } catch (error) {
            console.error("Error adding new reply:", error);
        }
    };

    return (
        <div>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                padding: '10px',
                backgroundColor: '#f0f0f0'
            }}>
                <h2>Home</h2>
                <div>
                    <button style={{marginRight: '10px'}} onClick={toggleModal}>Add Topic</button>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            </div>
            <TopicModal isOpen={modalOpen} onClose={toggleModal} onSubmit={handleSubmit} topic={newTopic}
                            onInputChange={handleInputChange} setSelectedFile={setSelectedFile}/>
            <ReplyModal isOpen={replyModalOpen} onClose={() => setReplyModalOpen(false)} onSubmit={handleReplySubmit}
                        reply={newReply} onInputChange={handleReplyInputChange} parentId={replyingToId} setSelectedFile={setSelectedFile}/>
            {messages.map((message) => (
                <div key={message.id}>
                    <MessageComponent message={message}/>
                    <button style={{float: "right"}} onClick={() => {
                        setReplyingToId(message.id);
                        setReplyModalOpen(true);
                    }}>Add Reply
                    </button>
                {/* Todo: Current positioning looks crappy, but it will do for now*/}
                </div>
            ))}
        </div>
    );
};

export default MessagePage;