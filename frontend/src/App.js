import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import axios from 'axios';

function HomePage() {
  return (
    <div>
      <h1>Welcome to the URL Shortener!</h1>
      <p>Get started by clicking <Link to="/shorten-url">here</Link></p>
    </div>
  );
}

function ShortenUrlPage() {
  const [url, setUrl] = useState('');
  const [shortUrl, setShortUrl] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();

    const response = await axios.post('/api/urls', { originalUrl: url });
    setShortUrl(response.data.shortUrl);
  };

  return (
    <div>
      <h1>URL Shortener</h1>
      <form onSubmit={handleSubmit}>
        <label>
          URL:
          <input type="text" value={url} onChange={(event) => setUrl(event.target.value)} />
        </label>
        <button type="submit">Shorten</button>
      </form>
      {shortUrl && (
        <div>
          <p>Shortened URL: <a href={shortUrl}>{shortUrl}</a></p>
        </div>
      )}
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/shorten-url" element={<ShortenUrlPage />} />
      </Routes>
    </Router>
  );
}

export default App;