import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PizzaOrder from './pizza-order';
import PizzaOrderDetail from './pizza-order-detail';
import PizzaOrderUpdate from './pizza-order-update';
import PizzaOrderDeleteDialog from './pizza-order-delete-dialog';

const PizzaOrderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PizzaOrder />} />
    <Route path="new" element={<PizzaOrderUpdate />} />
    <Route path=":id">
      <Route index element={<PizzaOrderDetail />} />
      <Route path="edit" element={<PizzaOrderUpdate />} />
      <Route path="delete" element={<PizzaOrderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PizzaOrderRoutes;
