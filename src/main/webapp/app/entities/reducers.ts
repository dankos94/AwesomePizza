import pizzaMenu from 'app/entities/pizza-menu/pizza-menu.reducer';
import menuSection from 'app/entities/menu-section/menu-section.reducer';
import dish from 'app/entities/dish/dish.reducer';
import pizzaOrder from 'app/entities/pizza-order/pizza-order.reducer';
import payment from 'app/entities/payment/payment.reducer';
import paymentMethod from 'app/entities/payment-method/payment-method.reducer';
import orderStatus from 'app/entities/order-status/order-status.reducer';
import paymentStatus from 'app/entities/payment-status/payment-status.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  pizzaMenu,
  menuSection,
  dish,
  pizzaOrder,
  payment,
  paymentMethod,
  orderStatus,
  paymentStatus,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
