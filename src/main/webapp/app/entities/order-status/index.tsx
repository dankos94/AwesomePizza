import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderStatus from './order-status';
import OrderStatusDetail from './order-status-detail';
import OrderStatusUpdate from './order-status-update';
import OrderStatusDeleteDialog from './order-status-delete-dialog';

const OrderStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderStatus />} />
    <Route path="new" element={<OrderStatusUpdate />} />
    <Route path=":id">
      <Route index element={<OrderStatusDetail />} />
      <Route path="edit" element={<OrderStatusUpdate />} />
      <Route path="delete" element={<OrderStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderStatusRoutes;
