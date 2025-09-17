import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, Typography, Box, Alert } from '@mui/material';

const TwoFASetupPage = () => {
  const [identifier, setIdentifier] = useState('');
  const [secret, setSecret] = useState('');
  const [otpauthUrl, setOtpauthUrl] = useState('');
  const [code, setCode] = useState('');
  const [step, setStep] = useState(1);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSetup = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await axios.post('/api/auth/2fa/setup', { identifier });
      setSecret(res.data.secret);
      setOtpauthUrl(res.data.otpauthUrl);
      setStep(2);
    } catch (err) {
      setError(err.response?.data || 'Setup failed');
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await axios.post('/api/auth/2fa/verify', { identifier, code });
      if (res.data.verified) {
        setSuccess(true);
      } else {
        setError('Invalid code');
      }
    } catch (err) {
      setError(err.response?.data || 'Verification failed');
    }
  };

  return (
    <Box maxWidth={400} mx="auto" mt={8}>
      <Typography variant="h5" mb={2}>2FA Setup</Typography>
      {error && <Alert severity="error">{error}</Alert>}
      {success && <Alert severity="success">2FA enabled successfully!</Alert>}
      {step === 1 && (
        <form onSubmit={handleSetup}>
          <TextField label="Username / Email / Phone" value={identifier} onChange={e => setIdentifier(e.target.value)} fullWidth margin="normal" required />
          <Button type="submit" variant="contained" color="primary" fullWidth>Start 2FA Setup</Button>
        </form>
      )}
      {step === 2 && (
        <>
          <Typography variant="body1" mt={2}>Scan this QR code in your authenticator app:</Typography>
          {otpauthUrl && (
            <Box my={2}>
              <img src={`https://api.qrserver.com/v1/create-qr-code/?data=${encodeURIComponent(otpauthUrl)}&size=200x200`} alt="QR Code" />
            </Box>
          )}
          <Typography variant="body2">Or enter this secret manually: <b>{secret}</b></Typography>
          <form onSubmit={handleVerify}>
            <TextField label="Enter code from app" value={code} onChange={e => setCode(e.target.value)} fullWidth margin="normal" required />
            <Button type="submit" variant="contained" color="primary" fullWidth>Verify & Enable 2FA</Button>
          </form>
        </>
      )}
    </Box>
  );
};

export default TwoFASetupPage;
