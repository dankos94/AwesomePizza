import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { Route } from 'react-router-dom';
import PizzaMenu from './pizza-menu';
import MenuSection from './menu-section';
import Dish from './dish';
import PizzaOrder from './pizza-order';
import Payment from './payment';
import PaymentMethod from './payment-method';
import OrderStatus from './order-status';
import PaymentStatus from './payment-status';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="pizza-menu/*" element={<PizzaMenu />} />
        <Route path="menu-section/*" element={<MenuSection />} />
        <Route path="dish/*" element={<Dish />} />
        <Route path="pizza-order/*" element={<PizzaOrder />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="payment-method/*" element={<PaymentMethod />} />
        <Route path="order-status/*" element={<OrderStatus />} />
        <Route path="payment-status/*" element={<PaymentStatus />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
