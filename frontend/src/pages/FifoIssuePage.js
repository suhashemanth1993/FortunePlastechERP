
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Button, TextField, MenuItem, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';

const RM_API = '/api/raw-materials';
const FIFO_API = '/api/issues/fifo-suggestion';
const ISSUE_API = '/api/issues';

function FifoIssuePage() {
  const [materials, setMaterials] = useState([]);
  const [selectedMaterial, setSelectedMaterial] = useState('');
  const [quantity, setQuantity] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [open, setOpen] = useState(false);
  const [productionOrderId, setProductionOrderId] = useState('');
  const [issued, setIssued] = useState([]);

  useEffect(() => {
    axios.get(RM_API).then(res => setMaterials(res.data));
  }, []);

  const fetchIssued = async () => {
    if (productionOrderId) {
      const res = await axios.get(`${ISSUE_API}/${productionOrderId}`);
      setIssued(res.data);
    }
  };

  const handleSuggest = async () => {
    if (!selectedMaterial || !quantity) return;
    const res = await axios.get(`${FIFO_API}?rawMaterialId=${selectedMaterial}&quantity=${quantity}`);
    setSuggestions(res.data);
    setOpen(true);
  };

  const handleIssue = async () => {
    await axios.post(ISSUE_API, {
      productionOrderId,
      rawMaterialId: selectedMaterial,
      quantity: parseFloat(quantity)
    });
    setOpen(false);
    setSuggestions([]);
    fetchIssued();
  };

  return (
    <div>
      <h2>FIFO Raw Material Issuing</h2>
      <TextField select label="Raw Material" value={selectedMaterial} onChange={e => setSelectedMaterial(e.target.value)} sx={{ mr: 2, minWidth: 200 }}>
        {materials.map(m => <MenuItem key={m.id} value={m.id}>{m.name}</MenuItem>)}
      </TextField>
      <TextField label="Quantity" value={quantity} onChange={e => setQuantity(e.target.value)} type="number" sx={{ mr: 2 }} />
      <TextField label="Production Order ID" value={productionOrderId} onChange={e => setProductionOrderId(e.target.value)} sx={{ mr: 2 }} />
      <Button variant="contained" onClick={handleSuggest}>Suggest FIFO Batches</Button>

      <Dialog open={open} onClose={() => setOpen(false)}>
        <DialogTitle>FIFO Batch Suggestions</DialogTitle>
        <DialogContent>
          <TableContainer component={Paper} sx={{ mt: 2 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Batch ID</TableCell>
                  <TableCell>Quantity Available</TableCell>
                  <TableCell>Expiry Date</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {suggestions.map((s, i) => (
                  <TableRow key={i}>
                    <TableCell>{s.batchId || s.batch_id}</TableCell>
                    <TableCell>{s.quantityAvailable || s.quantity_available}</TableCell>
                    <TableCell>{s.expiryDate || s.expiry_date}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button onClick={handleIssue} variant="contained">Issue Material</Button>
        </DialogActions>
      </Dialog>

      <h3>Issued Records</h3>
      <Button onClick={fetchIssued} variant="outlined" sx={{ mb: 1 }}>Refresh Issued</Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Issue ID</TableCell>
              <TableCell>Production Order</TableCell>
              <TableCell>Raw Material</TableCell>
              <TableCell>Quantity</TableCell>
              <TableCell>Issue Date</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {issued.map((i) => (
              <TableRow key={i.id}>
                <TableCell>{i.id}</TableCell>
                <TableCell>{i.productionOrderId}</TableCell>
                <TableCell>{i.rawMaterial?.name}</TableCell>
                <TableCell>{i.totalQuantity}</TableCell>
                <TableCell>{i.issueDate}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default FifoIssuePage;
