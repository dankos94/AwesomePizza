import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PizzaMenu from './pizza-menu';
import PizzaMenuDetail from './pizza-menu-detail';
import PizzaMenuUpdate from './pizza-menu-update';
import PizzaMenuDeleteDialog from './pizza-menu-delete-dialog';

const PizzaMenuRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PizzaMenu />} />
    <Route path="new" element={<PizzaMenuUpdate />} />
    <Route path=":id">
      <Route index element={<PizzaMenuDetail />} />
      <Route path="edit" element={<PizzaMenuUpdate />} />
      <Route path="delete" element={<PizzaMenuDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PizzaMenuRoutes;
