import React, { useState } from 'react';
import './App.css';
import LoginPage from './js/LoginPage.js';
import MessagePage from './js/messages/MessagePage.js';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLoginSuccess = () => {
    setIsLoggedIn(true);
  };

  return (
    <div className="App">
      {!isLoggedIn ? <LoginPage onLoginSuccess={handleLoginSuccess} /> : <MessagePage />}
    </div>
  );
}

export default App;
