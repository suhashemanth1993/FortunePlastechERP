
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Container } from '@mui/material';
import SupplierPage from './pages/SupplierPage';
import RawMaterialPage from './pages/RawMaterialPage';
import BatchPage from './pages/BatchPage';
import FifoIssuePage from './pages/FifoIssuePage';
import BinDashboardPage from './pages/BinDashboardPage';
import StockAlertPage from './pages/StockAlertPage';
import ExpiryTrackingPage from './pages/ExpiryTrackingPage';
import PurchaseOrderPage from './pages/PurchaseOrderPage';
import ReportingPage from './pages/ReportingPage';
import LoginPage from './pages/LoginPage';
import TwoFASetupPage from './pages/TwoFASetupPage';

function App() {
  return (
    <Router>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Fortune Plastech ERP
          </Typography>
          <Button color="inherit" component={Link} to="/suppliers">Suppliers</Button>
          <Button color="inherit" component={Link} to="/raw-materials">Raw Materials</Button>
          <Button color="inherit" component={Link} to="/batches">Batches</Button>
          <Button color="inherit" component={Link} to="/fifo-issue">FIFO Issue</Button>
          <Button color="inherit" component={Link} to="/bin-dashboard">Bin Dashboard</Button>
          <Button color="inherit" component={Link} to="/stock-alerts">Stock Alerts</Button>
          <Button color="inherit" component={Link} to="/expiry-tracking">Expiry Tracking</Button>
          <Button color="inherit" component={Link} to="/purchase-orders">Purchase Orders</Button>
          <Button color="inherit" component={Link} to="/reporting">Reporting</Button>
          <Button color="inherit" component={Link} to="/login">Login</Button>
          <Button color="inherit" component={Link} to="/2fa-setup">2FA Setup</Button>
        </Toolbar>
      </AppBar>
      <Container sx={{ mt: 4 }}>
        <Routes>
          <Route path="/suppliers" element={<SupplierPage />} />
          <Route path="/raw-materials" element={<RawMaterialPage />} />
          <Route path="/batches" element={<BatchPage />} />
          <Route path="/fifo-issue" element={<FifoIssuePage />} />
          <Route path="/bin-dashboard" element={<BinDashboardPage />} />
          <Route path="/stock-alerts" element={<StockAlertPage />} />
          <Route path="/expiry-tracking" element={<ExpiryTrackingPage />} />
          <Route path="/purchase-orders" element={<PurchaseOrderPage />} />
          <Route path="/reporting" element={<ReportingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/2fa-setup" element={<TwoFASetupPage />} />
          <Route path="*" element={<SupplierPage />} />
        </Routes>
      </Container>
    </Router>
  );
}

export default App;
