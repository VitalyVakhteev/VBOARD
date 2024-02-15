import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginPage = ({ onLoginSuccess }) => {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/users/login', { username, password });
            if (response.data === "User authenticated") {
                localStorage.setItem('isLoggedIn', 'true');
                navigate('/messages');
            } else {
                setErrorMessage("Incorrect username or password");
            }
        } catch (error) {
            if (error.response && error.response.data === "Invalid credentials") {
                setErrorMessage("Incorrect username or password");
            } else {
                setErrorMessage("An error has occurred. Please try again later.");
            }
        }
    };

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleLogin}>
                <div>
                    <label>Username: </label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
                </div>
                <div>
                    <label>Password: </label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <button type="submit">Login</button>
                {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            </form>
        </div>
    );
};

export default LoginPage;