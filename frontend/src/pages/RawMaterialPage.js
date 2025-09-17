
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from '@mui/material';
import { Edit, Delete } from '@mui/icons-material';

const API_URL = '/api/raw-materials';

function RawMaterialPage() {
  const [materials, setMaterials] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({
    name: '', code: '', category: '', specification: '', unit: '', standardCost: '', minimumStockLevel: ''
  });

  const fetchMaterials = async () => {
    const res = await axios.get(API_URL);
    setMaterials(res.data);
  };

  useEffect(() => { fetchMaterials(); }, []);

  const handleOpen = (material = null) => {
    setEditing(material);
    setForm(material || { name: '', code: '', category: '', specification: '', unit: '', standardCost: '', minimumStockLevel: '' });
    setOpen(true);
  };
  const handleClose = () => { setOpen(false); setEditing(null); };

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async () => {
    const payload = { ...form, standardCost: parseFloat(form.standardCost), minimumStockLevel: parseFloat(form.minimumStockLevel) };
    if (editing) {
      await axios.put(`${API_URL}/${editing.id}`, payload);
    } else {
      await axios.post(API_URL, payload);
    }
    fetchMaterials();
    handleClose();
  };

  const handleDelete = async id => {
    await axios.delete(`${API_URL}/${id}`);
    fetchMaterials();
  };

  return (
    <div>
      <h2>Raw Materials</h2>
      <Button variant="contained" color="primary" onClick={() => handleOpen()}>Add Raw Material</Button>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Code</TableCell>
              <TableCell>Category</TableCell>
              <TableCell>Specification</TableCell>
              <TableCell>Unit</TableCell>
              <TableCell>Standard Cost</TableCell>
              <TableCell>Min Stock Level</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {materials.map(m => (
              <TableRow key={m.id}>
                <TableCell>{m.name}</TableCell>
                <TableCell>{m.code}</TableCell>
                <TableCell>{m.category}</TableCell>
                <TableCell>{m.specification}</TableCell>
                <TableCell>{m.unit}</TableCell>
                <TableCell>{m.standardCost}</TableCell>
                <TableCell>{m.minimumStockLevel}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleOpen(m)}><Edit /></IconButton>
                  <IconButton onClick={() => handleDelete(m.id)}><Delete /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{editing ? 'Edit Raw Material' : 'Add Raw Material'}</DialogTitle>
        <DialogContent>
          <TextField margin="dense" label="Name" name="name" value={form.name} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Code" name="code" value={form.code} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Category" name="category" value={form.category} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Specification" name="specification" value={form.specification} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Unit" name="unit" value={form.unit} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Standard Cost" name="standardCost" value={form.standardCost} onChange={handleChange} fullWidth type="number" />
          <TextField margin="dense" label="Min Stock Level" name="minimumStockLevel" value={form.minimumStockLevel} onChange={handleChange} fullWidth type="number" />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">{editing ? 'Update' : 'Add'}</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default RawMaterialPage;
