import React from 'react';
import {BrowserRouter as Router, Route, Navigate, Routes} from 'react-router-dom';
import './App.css';
import LoginPage from './js/LoginPage.js';
import MessagePage from './js/messages/MessagePage.js';

function App() {
    const isLoggedIn = localStorage.getItem('isLoggedIn');

  return (
      <Router>
          <Routes>
              <Route path="/login" element={isLoggedIn ? <Navigate replace to="/messages" /> : <LoginPage />} />
              <Route path="/messages" element={isLoggedIn ? <MessagePage /> : <Navigate replace to="/login" />} />
              <Route path="/" element={<Navigate replace to={isLoggedIn ? "/messages" : "/login"} />} />
          </Routes>
      </Router>
  );
}

export default App;
