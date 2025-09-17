
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, TextField, MenuItem, Button } from '@mui/material';

const API_URL = '/api/bin-dashboard';
const RM_API = '/api/raw-materials';

function BinDashboardPage() {
  const [stock, setStock] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [rawMaterialId, setRawMaterialId] = useState('');
  const [location, setLocation] = useState('');

  const fetchStock = async () => {
    let url = API_URL;
    const params = [];
    if (rawMaterialId) params.push(`rawMaterialId=${rawMaterialId}`);
    if (location) params.push(`location=${encodeURIComponent(location)}`);
    if (params.length) url += '?' + params.join('&');
    const res = await axios.get(url);
    setStock(res.data);
  };

  useEffect(() => {
    axios.get(RM_API).then(res => setMaterials(res.data));
    fetchStock();
    // eslint-disable-next-line
  }, []);

  return (
    <div>
      <h2>Bin Dashboard</h2>
      <TextField select label="Raw Material" value={rawMaterialId} onChange={e => setRawMaterialId(e.target.value)} sx={{ mr: 2, minWidth: 200 }}>
        <MenuItem value="">All</MenuItem>
        {materials.map(m => <MenuItem key={m.id} value={m.id}>{m.name}</MenuItem>)}
      </TextField>
      <TextField label="Location" value={location} onChange={e => setLocation(e.target.value)} sx={{ mr: 2 }} />
      <Button variant="contained" onClick={fetchStock}>Filter</Button>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Raw Material</TableCell>
              <TableCell>Batch</TableCell>
              <TableCell>Location</TableCell>
              <TableCell>Quantity</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {stock.map((s, i) => (
              <TableRow key={i}>
                <TableCell>{s.rawMaterialName || s.raw_material_name}</TableCell>
                <TableCell>{s.batchId || s.batch_id}</TableCell>
                <TableCell>{s.location}</TableCell>
                <TableCell>{s.quantity}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default BinDashboardPage;
