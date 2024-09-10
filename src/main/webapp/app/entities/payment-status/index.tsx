import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentStatus from './payment-status';
import PaymentStatusDetail from './payment-status-detail';
import PaymentStatusUpdate from './payment-status-update';
import PaymentStatusDeleteDialog from './payment-status-delete-dialog';

const PaymentStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentStatus />} />
    <Route path="new" element={<PaymentStatusUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentStatusDetail />} />
      <Route path="edit" element={<PaymentStatusUpdate />} />
      <Route path="delete" element={<PaymentStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentStatusRoutes;
