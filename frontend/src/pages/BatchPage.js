
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, MenuItem } from '@mui/material';
import { Edit, Delete } from '@mui/icons-material';

const API_URL = '/api/batches';
const RM_API = '/api/raw-materials';
const SUP_API = '/api/suppliers';

function BatchPage() {
  const [batches, setBatches] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({
    rawMaterialId: '', supplierId: '', quantity: '', costPerUnit: '', receiptDate: ''
  });

  const fetchAll = async () => {
    const [b, m, s] = await Promise.all([
      axios.get(API_URL), axios.get(RM_API), axios.get(SUP_API)
    ]);
    setBatches(b.data); setMaterials(m.data); setSuppliers(s.data);
  };
  useEffect(() => { fetchAll(); }, []);

  const handleOpen = (batch = null) => {
    setEditing(batch);
    setForm(batch ? {
      rawMaterialId: batch.rawMaterial?.id || '',
      supplierId: batch.supplier?.id || '',
      quantity: batch.quantity,
      costPerUnit: batch.costPerUnit,
      receiptDate: batch.receiptDate || ''
    } : { rawMaterialId: '', supplierId: '', quantity: '', costPerUnit: '', receiptDate: '' });
    setOpen(true);
  };
  const handleClose = () => { setOpen(false); setEditing(null); };
  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async () => {
    if (editing) {
      await axios.put(`${API_URL}/${editing.id}?quantity=${form.quantity}&costPerUnit=${form.costPerUnit}`);
    } else {
      await axios.post(`${API_URL}?rawMaterialId=${form.rawMaterialId}&supplierId=${form.supplierId}&quantity=${form.quantity}&costPerUnit=${form.costPerUnit}&receiptDate=${form.receiptDate}`);
    }
    fetchAll();
    handleClose();
  };
  const handleDelete = async id => {
    await axios.delete(`${API_URL}/${id}`);
    fetchAll();
  };

  return (
    <div>
      <h2>Batches</h2>
      <Button variant="contained" color="primary" onClick={() => handleOpen()}>Add Batch</Button>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Raw Material</TableCell>
              <TableCell>Supplier</TableCell>
              <TableCell>Quantity</TableCell>
              <TableCell>Cost/Unit</TableCell>
              <TableCell>Receipt Date</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {batches.map(b => (
              <TableRow key={b.id}>
                <TableCell>{b.id}</TableCell>
                <TableCell>{b.rawMaterial?.name}</TableCell>
                <TableCell>{b.supplier?.name}</TableCell>
                <TableCell>{b.quantity}</TableCell>
                <TableCell>{b.costPerUnit}</TableCell>
                <TableCell>{b.receiptDate}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleOpen(b)}><Edit /></IconButton>
                  <IconButton onClick={() => handleDelete(b.id)}><Delete /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{editing ? 'Edit Batch' : 'Add Batch'}</DialogTitle>
        <DialogContent>
          <TextField select margin="dense" label="Raw Material" name="rawMaterialId" value={form.rawMaterialId} onChange={handleChange} fullWidth disabled={!!editing}>
            {materials.map(m => <MenuItem key={m.id} value={m.id}>{m.name}</MenuItem>)}
          </TextField>
          <TextField select margin="dense" label="Supplier" name="supplierId" value={form.supplierId} onChange={handleChange} fullWidth disabled={!!editing}>
            {suppliers.map(s => <MenuItem key={s.id} value={s.id}>{s.name}</MenuItem>)}
          </TextField>
          <TextField margin="dense" label="Quantity" name="quantity" value={form.quantity} onChange={handleChange} fullWidth type="number" />
          <TextField margin="dense" label="Cost Per Unit" name="costPerUnit" value={form.costPerUnit} onChange={handleChange} fullWidth type="number" />
          <TextField margin="dense" label="Receipt Date" name="receiptDate" value={form.receiptDate} onChange={handleChange} fullWidth type="date" InputLabelProps={{ shrink: true }} />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">{editing ? 'Update' : 'Add'}</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default BatchPage;
