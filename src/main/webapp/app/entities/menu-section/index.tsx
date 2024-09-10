import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MenuSection from './menu-section';
import MenuSectionDetail from './menu-section-detail';
import MenuSectionUpdate from './menu-section-update';
import MenuSectionDeleteDialog from './menu-section-delete-dialog';

const MenuSectionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MenuSection />} />
    <Route path="new" element={<MenuSectionUpdate />} />
    <Route path=":id">
      <Route index element={<MenuSectionDetail />} />
      <Route path="edit" element={<MenuSectionUpdate />} />
      <Route path="delete" element={<MenuSectionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MenuSectionRoutes;
