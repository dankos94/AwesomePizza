import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dish.reducer';

export const DishDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dishEntity = useAppSelector(state => state.dish.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dishDetailsHeading">
          <Translate contentKey="awesomePizzaApp.dish.detail.title">Dish</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dishEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="awesomePizzaApp.dish.name">Name</Translate>
            </span>
          </dt>
          <dd>{dishEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="awesomePizzaApp.dish.description">Description</Translate>
            </span>
          </dt>
          <dd>{dishEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="awesomePizzaApp.dish.price">Price</Translate>
            </span>
          </dt>
          <dd>{dishEntity.price}</dd>
          <dt>
            <span id="available">
              <Translate contentKey="awesomePizzaApp.dish.available">Available</Translate>
            </span>
          </dt>
          <dd>{dishEntity.available ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.dish.menuSection">Menu Section</Translate>
          </dt>
          <dd>{dishEntity.menuSection ? dishEntity.menuSection.id : ''}</dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.dish.pizzaOrder">Pizza Order</Translate>
          </dt>
          <dd>
            {dishEntity.pizzaOrders
              ? dishEntity.pizzaOrders.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {dishEntity.pizzaOrders && i === dishEntity.pizzaOrders.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/dish" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dish/${dishEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DishDetail;
