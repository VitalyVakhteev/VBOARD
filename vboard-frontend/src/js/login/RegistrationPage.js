import React, { useState } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const RegistrationPage = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleRegistration = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setErrorMessage("Passwords do not match");
            return;
        }
        try {
            const response = await axios.post('http://localhost:8080/api/users/register', { username, password, confirmPassword });
            alert(response.data);
            navigate('/login');
        } catch (error) {
            setErrorMessage(error.response.data);
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
            <h2>Register</h2>
            <form onSubmit={handleRegistration} style={{
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
                <div style={{marginBottom: "20px", width: "100%"}}>
                    <label style={{display: "block", marginBottom: "5px"}}>Confirm Password: </label>
                    <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}
                           style={{width: "100%", padding: "8px"}}/>
                </div>
                <button type="submit" style={{padding: "8px 20px", cursor: "pointer"}}>Register</button>
                {errorMessage && <div style={{color: 'red', marginTop: "10px"}}>{errorMessage}</div>}
            </form>
        </div>
    );
};

export default RegistrationPage;
