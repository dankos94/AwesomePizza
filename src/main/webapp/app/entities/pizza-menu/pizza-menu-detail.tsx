import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pizza-menu.reducer';

export const PizzaMenuDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pizzaMenuEntity = useAppSelector(state => state.pizzaMenu.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pizzaMenuDetailsHeading">
          <Translate contentKey="awesomePizzaApp.pizzaMenu.detail.title">PizzaMenu</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pizzaMenuEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="awesomePizzaApp.pizzaMenu.name">Name</Translate>
            </span>
          </dt>
          <dd>{pizzaMenuEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="awesomePizzaApp.pizzaMenu.description">Description</Translate>
            </span>
          </dt>
          <dd>{pizzaMenuEntity.description}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="awesomePizzaApp.pizzaMenu.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {pizzaMenuEntity.createdDate ? <TextFormat value={pizzaMenuEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.pizzaMenu.user">User</Translate>
          </dt>
          <dd>{pizzaMenuEntity.user ? pizzaMenuEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pizza-menu" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pizza-menu/${pizzaMenuEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PizzaMenuDetail;
