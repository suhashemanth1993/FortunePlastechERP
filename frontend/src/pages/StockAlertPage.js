
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';

const API_URL = '/api/stock-alerts/minimum';

function StockAlertPage() {
  const [alerts, setAlerts] = useState([]);

  const fetchAlerts = async () => {
    const res = await axios.get(API_URL);
    setAlerts(res.data);
  };

  useEffect(() => { fetchAlerts(); }, []);

  return (
    <div>
      <h2>Minimum Stock Alerts</h2>
      <Button variant="outlined" onClick={fetchAlerts} sx={{ mb: 2 }}>Refresh</Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Raw Material</TableCell>
              <TableCell>Current Stock</TableCell>
              <TableCell>Minimum Stock Level</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {alerts.map((a, i) => (
              <TableRow key={i}>
                <TableCell>{a.rawMaterialName || a.raw_material_name}</TableCell>
                <TableCell>{a.currentStock || a.current_stock}</TableCell>
                <TableCell>{a.minimumStockLevel || a.minimum_stock_level}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default StockAlertPage;
