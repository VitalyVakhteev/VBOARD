import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Navigate, Routes } from 'react-router-dom';
import LoginPage from './js/login/LoginPage';
import MessagePage from './js/messages/MessagePage';
import RegistrationPage from "./js/login/RegistrationPage";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem('isLoggedIn') === 'true');

    useEffect(() => {
        const handleStorageChange = () => {
            setIsLoggedIn(localStorage.getItem('isLoggedIn') === 'true');
        };

        window.addEventListener('storage', handleStorageChange);
        return () => window.removeEventListener('storage', handleStorageChange);
    }, []);

    return (
        <Router>
            <Routes>
                <Route path="/login" element={!isLoggedIn ? <LoginPage setIsLoggedIn={setIsLoggedIn} /> : <Navigate replace to="/messages" />} />
                <Route path="/messages" element={isLoggedIn ? <MessagePage setIsLoggedIn={setIsLoggedIn} /> : <Navigate replace to="/login" />} />
                <Route path="/register" element={!isLoggedIn ? <RegistrationPage /> : <Navigate replace to="/messages" />} />
                <Route path="/" element={<Navigate replace to={isLoggedIn ? "/messages" : "/login"} />} />
            </Routes>
        </Router>
    );
}

export default App;
