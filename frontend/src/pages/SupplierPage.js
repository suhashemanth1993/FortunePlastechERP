
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from '@mui/material';
import { Edit, Delete } from '@mui/icons-material';

const API_URL = '/api/suppliers';

function SupplierPage() {
  const [suppliers, setSuppliers] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({
    name: '', contactPerson: '', contactNumber: '', email: '', address: '', gstNumber: '', paymentTerms: ''
  });

  const fetchSuppliers = async () => {
    const res = await axios.get(API_URL);
    setSuppliers(res.data);
  };

  useEffect(() => { fetchSuppliers(); }, []);

  const handleOpen = (supplier = null) => {
    setEditing(supplier);
    setForm(supplier || { name: '', contactPerson: '', contactNumber: '', email: '', address: '', gstNumber: '', paymentTerms: '' });
    setOpen(true);
  };
  const handleClose = () => { setOpen(false); setEditing(null); };

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async () => {
    if (editing) {
      await axios.put(`${API_URL}/${editing.id}`, form);
    } else {
      await axios.post(API_URL, form);
    }
    fetchSuppliers();
    handleClose();
  };

  const handleDelete = async id => {
    await axios.delete(`${API_URL}/${id}`);
    fetchSuppliers();
  };

  return (
    <div>
      <h2>Suppliers</h2>
      <Button variant="contained" color="primary" onClick={() => handleOpen()}>Add Supplier</Button>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Contact Person</TableCell>
              <TableCell>Contact Number</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Address</TableCell>
              <TableCell>GST Number</TableCell>
              <TableCell>Payment Terms</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {suppliers.map(s => (
              <TableRow key={s.id}>
                <TableCell>{s.name}</TableCell>
                <TableCell>{s.contactPerson}</TableCell>
                <TableCell>{s.contactNumber}</TableCell>
                <TableCell>{s.email}</TableCell>
                <TableCell>{s.address}</TableCell>
                <TableCell>{s.gstNumber}</TableCell>
                <TableCell>{s.paymentTerms}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleOpen(s)}><Edit /></IconButton>
                  <IconButton onClick={() => handleDelete(s.id)}><Delete /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{editing ? 'Edit Supplier' : 'Add Supplier'}</DialogTitle>
        <DialogContent>
          <TextField margin="dense" label="Name" name="name" value={form.name} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Contact Person" name="contactPerson" value={form.contactPerson} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Contact Number" name="contactNumber" value={form.contactNumber} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Email" name="email" value={form.email} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Address" name="address" value={form.address} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="GST Number" name="gstNumber" value={form.gstNumber} onChange={handleChange} fullWidth />
          <TextField margin="dense" label="Payment Terms" name="paymentTerms" value={form.paymentTerms} onChange={handleChange} fullWidth />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">{editing ? 'Update' : 'Add'}</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default SupplierPage;
