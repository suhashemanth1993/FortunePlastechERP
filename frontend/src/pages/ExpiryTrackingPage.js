
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, TextField } from '@mui/material';

const API_URL = '/api/expiry-alerts';

function ExpiryTrackingPage() {
  const [batches, setBatches] = useState([]);
  const [days, setDays] = useState(30);

  const fetchBatches = async () => {
    const res = await axios.get(`${API_URL}?daysThreshold=${days}`);
    setBatches(res.data);
  };

  useEffect(() => { fetchBatches(); }, []);

  return (
    <div>
      <h2>Expiry/Shelf-life Tracking</h2>
      <TextField label="Days Threshold" type="number" value={days} onChange={e => setDays(e.target.value)} sx={{ mr: 2 }} />
      <Button variant="outlined" onClick={fetchBatches} sx={{ mb: 2 }}>Refresh</Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Batch ID</TableCell>
              <TableCell>Raw Material</TableCell>
              <TableCell>Quantity</TableCell>
              <TableCell>Expiry Date</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {batches.map((b, i) => (
              <TableRow key={i}>
                <TableCell>{b.batchId || b.batch_id}</TableCell>
                <TableCell>{b.rawMaterialName || b.raw_material_name}</TableCell>
                <TableCell>{b.quantity}</TableCell>
                <TableCell>{b.expiryDate || b.expiry_date}</TableCell>
                <TableCell>{b.status}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default ExpiryTrackingPage;
