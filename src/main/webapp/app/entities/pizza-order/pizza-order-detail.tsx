import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pizza-order.reducer';

export const PizzaOrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pizzaOrderEntity = useAppSelector(state => state.pizzaOrder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pizzaOrderDetailsHeading">
          <Translate contentKey="awesomePizzaApp.pizzaOrder.detail.title">PizzaOrder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pizzaOrderEntity.id}</dd>
          <dt>
            <span id="orderDate">
              <Translate contentKey="awesomePizzaApp.pizzaOrder.orderDate">Order Date</Translate>
            </span>
          </dt>
          <dd>
            {pizzaOrderEntity.orderDate ? <TextFormat value={pizzaOrderEntity.orderDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="totalPrice">
              <Translate contentKey="awesomePizzaApp.pizzaOrder.totalPrice">Total Price</Translate>
            </span>
          </dt>
          <dd>{pizzaOrderEntity.totalPrice}</dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.pizzaOrder.payment">Payment</Translate>
          </dt>
          <dd>{pizzaOrderEntity.payment ? pizzaOrderEntity.payment.id : ''}</dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.pizzaOrder.user">User</Translate>
          </dt>
          <dd>{pizzaOrderEntity.user ? pizzaOrderEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.pizzaOrder.orderStatus">Order Status</Translate>
          </dt>
          <dd>{pizzaOrderEntity.orderStatus ? pizzaOrderEntity.orderStatus.id : ''}</dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.pizzaOrder.dish">Dish</Translate>
          </dt>
          <dd>
            {pizzaOrderEntity.dishes
              ? pizzaOrderEntity.dishes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {pizzaOrderEntity.dishes && i === pizzaOrderEntity.dishes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/pizza-order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pizza-order/${pizzaOrderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PizzaOrderDetail;
