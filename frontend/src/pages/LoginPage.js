import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, Typography, Box, Alert } from '@mui/material';

const LoginPage = () => {
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [twoFactorEnabled, setTwoFactorEnabled] = useState(false);
  const [show2fa, setShow2fa] = useState(false);
  const [code, setCode] = useState('');
  const [token, setToken] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setShow2fa(false);
    try {
      const res = await axios.post('/api/auth/login', { identifier, password });
      if (res.data.twoFactorEnabled) {
        setShow2fa(true);
        setToken(res.data.token);
        setTwoFactorEnabled(true);
      } else {
        setToken(res.data.token);
        // handle successful login (redirect, store token, etc)
      }
    } catch (err) {
      setError(err.response?.data || 'Login failed');
    }
  };

  const handle2fa = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await axios.post('/api/auth/2fa/verify', { identifier, code });
      if (res.data.verified) {
        // handle successful 2FA login (redirect, store token, etc)
      } else {
        setError('Invalid 2FA code');
      }
    } catch (err) {
      setError(err.response?.data || '2FA verification failed');
    }
  };

  return (
    <Box maxWidth={400} mx="auto" mt={8}>
      <Typography variant="h5" mb={2}>Login</Typography>
      {error && <Alert severity="error">{error}</Alert>}
      {!show2fa ? (
        <form onSubmit={handleLogin}>
          <TextField label="Username / Email / Phone" value={identifier} onChange={e => setIdentifier(e.target.value)} fullWidth margin="normal" required />
          <TextField label="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} fullWidth margin="normal" required />
          <Button type="submit" variant="contained" color="primary" fullWidth>Login</Button>
        </form>
      ) : (
        <form onSubmit={handle2fa}>
          <TextField label="2FA Code" value={code} onChange={e => setCode(e.target.value)} fullWidth margin="normal" required />
          <Button type="submit" variant="contained" color="primary" fullWidth>Verify 2FA</Button>
        </form>
      )}
    </Box>
  );
};

export default LoginPage;
