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
        <div>
            <h2>Register</h2>
            <form onSubmit={handleRegistration}>
                <div>
                    <label>Username: </label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
                </div>
                <div>
                    <label>Password: </label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <div>
                    <label>Confirm Password: </label>
                    <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
                </div>
                <button type="submit">Register</button>
                {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            </form>
        </div>
    );
};

export default RegistrationPage;
