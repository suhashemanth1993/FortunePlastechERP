
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, MenuItem, IconButton } from '@mui/material';
import { Link } from 'react-router-dom';

const API_URL = '/api/purchase-orders';
const SUP_API = '/api/suppliers';
const RM_API = '/api/raw-materials';
const BATCH_API = '/api/batches';

function PurchaseOrderPage() {
  const [orders, setOrders] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [batches, setBatches] = useState([]);
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState({ supplierId: '', lineItems: [{ rawMaterialId: '', quantity: '', unitPrice: '' }] });
  const [linkDialog, setLinkDialog] = useState(false);
  const [selectedPO, setSelectedPO] = useState(null);
  const [selectedBatch, setSelectedBatch] = useState('');

  const fetchAll = async () => {
    const [o, s, m, b] = await Promise.all([
      axios.get(API_URL), axios.get(SUP_API), axios.get(RM_API), axios.get(BATCH_API)
    ]);
    setOrders(o.data); setSuppliers(s.data); setMaterials(m.data); setBatches(b.data);
  };
  useEffect(() => { fetchAll(); }, []);

  const handleOpen = () => {
    setForm({ supplierId: '', lineItems: [{ rawMaterialId: '', quantity: '', unitPrice: '' }] });
    setOpen(true);
  };
  const handleClose = () => setOpen(false);
  const handleFormChange = (e, idx = null) => {
    if (idx === null) setForm({ ...form, [e.target.name]: e.target.value });
    else {
      const items = [...form.lineItems];
      items[idx][e.target.name] = e.target.value;
      setForm({ ...form, lineItems: items });
    }
  };
  const addLineItem = () => setForm({ ...form, lineItems: [...form.lineItems, { rawMaterialId: '', quantity: '', unitPrice: '' }] });
  const removeLineItem = idx => setForm({ ...form, lineItems: form.lineItems.filter((_, i) => i !== idx) });

  const handleSubmit = async () => {
    const payload = {
      supplierId: form.supplierId,
      lineItems: form.lineItems.map(li => ({
        rawMaterialId: li.rawMaterialId,
        quantity: parseFloat(li.quantity),
        unitPrice: parseFloat(li.unitPrice)
      }))
    };
    await axios.post(API_URL, payload);
    fetchAll();
    handleClose();
  };

  const handleLinkBatch = async () => {
    await axios.post(`${API_URL}/link-batch?batchId=${selectedBatch}&poId=${selectedPO}`);
    setLinkDialog(false);
    fetchAll();
  };

  return (
    <div>
      <h2>Purchase Orders & Receipts</h2>
      <Button variant="contained" color="primary" onClick={handleOpen}>Create Purchase Order</Button>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Supplier</TableCell>
              <TableCell>Order Date</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Line Items</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map(o => (
              <TableRow key={o.id}>
                <TableCell>{o.id}</TableCell>
                <TableCell>{o.supplier?.name}</TableCell>
                <TableCell>{o.orderDate}</TableCell>
                <TableCell>{o.status}</TableCell>
                <TableCell>
                  <ul>
                    {(o.lineItems || []).map((li, i) => (
                      <li key={i}>{li.rawMaterial?.name} - Qty: {li.quantity} @ {li.unitPrice}</li>
                    ))}
                  </ul>
                </TableCell>
                <TableCell>
                  <Button size="small" onClick={() => { setSelectedPO(o.id); setLinkDialog(true); }}>Link Batch</Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Create PO Dialog */}
      <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
        <DialogTitle>Create Purchase Order</DialogTitle>
        <DialogContent>
          <TextField select label="Supplier" name="supplierId" value={form.supplierId} onChange={handleFormChange} fullWidth sx={{ mb: 2 }}>
            {suppliers.map(s => <MenuItem key={s.id} value={s.id}>{s.name}</MenuItem>)}
          </TextField>
          {form.lineItems.map((li, idx) => (
            <div key={idx} style={{ display: 'flex', gap: 8, marginBottom: 8 }}>
              <TextField select label="Raw Material" name="rawMaterialId" value={li.rawMaterialId} onChange={e => handleFormChange(e, idx)} sx={{ minWidth: 180 }}>
                {materials.map(m => <MenuItem key={m.id} value={m.id}>{m.name}</MenuItem>)}
              </TextField>
              <TextField label="Quantity" name="quantity" value={li.quantity} onChange={e => handleFormChange(e, idx)} type="number" sx={{ minWidth: 120 }} />
              <TextField label="Unit Price" name="unitPrice" value={li.unitPrice} onChange={e => handleFormChange(e, idx)} type="number" sx={{ minWidth: 120 }} />
              <Button onClick={() => removeLineItem(idx)} disabled={form.lineItems.length === 1}>Remove</Button>
            </div>
          ))}
          <Button onClick={addLineItem}>Add Line Item</Button>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">Create</Button>
        </DialogActions>
      </Dialog>

      {/* Link Batch Dialog */}
      <Dialog open={linkDialog} onClose={() => setLinkDialog(false)}>
        <DialogTitle>Link Batch to PO</DialogTitle>
        <DialogContent>
          <TextField select label="Batch" value={selectedBatch} onChange={e => setSelectedBatch(e.target.value)} fullWidth>
            {batches.map(b => <MenuItem key={b.id} value={b.id}>{b.id} - {b.rawMaterial?.name} ({b.quantity})</MenuItem>)}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setLinkDialog(false)}>Cancel</Button>
          <Button onClick={handleLinkBatch} variant="contained">Link</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default PurchaseOrderPage;
