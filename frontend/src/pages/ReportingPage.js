
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Tabs, Tab, TextField, MenuItem } from '@mui/material';

const STOCK_API = '/api/reports/stock-summary';
const SUPPLIER_API = '/api/reports/supplier-purchase';
const COSTING_API = '/api/reports/costing';
const SUP_API = '/api/suppliers';

function ReportingPage() {
  const [tab, setTab] = useState(0);
  const [stock, setStock] = useState([]);
  const [supplierReport, setSupplierReport] = useState([]);
  const [costing, setCosting] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [supplierId, setSupplierId] = useState('');

  useEffect(() => {
    axios.get(SUP_API).then(res => setSuppliers(res.data));
    fetchStock();
    // eslint-disable-next-line
  }, []);

  const fetchStock = async () => {
    const res = await axios.get(STOCK_API);
    setStock(res.data);
  };
  const fetchSupplierReport = async () => {
    let url = SUPPLIER_API;
    if (supplierId) url += `?supplierId=${supplierId}`;
    const res = await axios.get(url);
    setSupplierReport(res.data);
  };
  const fetchCosting = async () => {
    const res = await axios.get(COSTING_API);
    setCosting(res.data);
  };

  return (
    <div>
      <h2>Reporting</h2>
      <Tabs value={tab} onChange={(_, v) => setTab(v)} sx={{ mb: 2 }}>
        <Tab label="Stock Summary" />
        <Tab label="Supplier Purchase" />
        <Tab label="Costing" />
      </Tabs>
      {tab === 0 && (
        <div>
          <Button variant="outlined" onClick={fetchStock} sx={{ mb: 2 }}>Refresh</Button>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Raw Material</TableCell>
                  <TableCell>Total Stock</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {stock.map((s, i) => (
                  <TableRow key={i}>
                    <TableCell>{s.rawMaterialName || s.raw_material_name}</TableCell>
                    <TableCell>{s.totalStock || s.total_stock}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      )}
      {tab === 1 && (
        <div>
          <TextField select label="Supplier" value={supplierId} onChange={e => setSupplierId(e.target.value)} sx={{ mr: 2, minWidth: 200 }}>
            <MenuItem value="">All</MenuItem>
            {suppliers.map(s => <MenuItem key={s.id} value={s.id}>{s.name}</MenuItem>)}
          </TextField>
          <Button variant="outlined" onClick={fetchSupplierReport} sx={{ mb: 2 }}>Refresh</Button>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Supplier</TableCell>
                  <TableCell>Raw Material</TableCell>
                  <TableCell>Quantity</TableCell>
                  <TableCell>Total Cost</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {supplierReport.map((r, i) => (
                  <TableRow key={i}>
                    <TableCell>{r.supplierName || r.supplier_name}</TableCell>
                    <TableCell>{r.rawMaterialName || r.raw_material_name}</TableCell>
                    <TableCell>{r.quantity}</TableCell>
                    <TableCell>{r.totalCost || r.total_cost}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      )}
      {tab === 2 && (
        <div>
          <Button variant="outlined" onClick={fetchCosting} sx={{ mb: 2 }}>Refresh</Button>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Raw Material</TableCell>
                  <TableCell>Cost per Unit</TableCell>
                  <TableCell>Total Cost</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {costing.map((c, i) => (
                  <TableRow key={i}>
                    <TableCell>{c.rawMaterialName || c.raw_material_name}</TableCell>
                    <TableCell>{c.costPerUnit || c.cost_per_unit}</TableCell>
                    <TableCell>{c.totalCost || c.total_cost}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      )}
    </div>
  );
}

export default ReportingPage;
