import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate } from 'react-jhipster';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/pizza-menu">
        <Translate contentKey="global.menu.entities.pizzaMenu" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/menu-section">
        <Translate contentKey="global.menu.entities.menuSection" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dish">
        <Translate contentKey="global.menu.entities.dish" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pizza-order">
        <Translate contentKey="global.menu.entities.pizzaOrder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.payment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-method">
        <Translate contentKey="global.menu.entities.paymentMethod" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-status">
        <Translate contentKey="global.menu.entities.orderStatus" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-status">
        <Translate contentKey="global.menu.entities.paymentStatus" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
