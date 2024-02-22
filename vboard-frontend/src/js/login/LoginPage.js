import React, { useState } from 'react';
import axios from 'axios';
import {Link, useNavigate} from 'react-router-dom';

const LoginPage = ({ setIsLoggedIn }) => {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const REACT_APP_API_HOST = process.env.REACT_APP_API_HOST;

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(REACT_APP_API_HOST + '/api/users/login', { username, password });
            if (response.data === "User authenticated") {
                localStorage.setItem('isLoggedIn', 'true');
                localStorage.setItem('currentUsername', username);
                setIsLoggedIn(true);
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
        <div style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "flex-start",
            minHeight: "100vh"
        }}>
            <h2>Login</h2>
            <form onSubmit={handleLogin} style={{
                width: "100%",
                maxWidth: "300px",
                display: "flex",
                flexDirection: "column",
                alignItems: "center"
            }}>
                <div style={{marginBottom: "20px", width: "100%"}}>
                    <label style={{display: "block", marginBottom: "5px"}}>Username: </label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}
                           style={{width: "100%", padding: "8px"}}/>
                </div>
                <div style={{marginBottom: "20px", width: "100%"}}>
                    <label style={{display: "block", marginBottom: "5px"}}>Password: </label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                           style={{width: "100%", padding: "8px"}}/>
                </div>
                <button type="submit" style={{padding: "8px 20px", cursor: "pointer"}}>Login</button>
                {errorMessage && <div style={{color: 'red', marginTop: "10px"}}>{errorMessage}</div>}
            </form>
            <p style={{color: 'blue', marginTop: "20px"}}> Don't have an account? <Link to="/register">Register</Link>
            </p>
        </div>
    );
};

export default LoginPage;