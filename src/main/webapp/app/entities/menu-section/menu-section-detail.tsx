import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './menu-section.reducer';

export const MenuSectionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const menuSectionEntity = useAppSelector(state => state.menuSection.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="menuSectionDetailsHeading">
          <Translate contentKey="awesomePizzaApp.menuSection.detail.title">MenuSection</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{menuSectionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="awesomePizzaApp.menuSection.name">Name</Translate>
            </span>
          </dt>
          <dd>{menuSectionEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="awesomePizzaApp.menuSection.description">Description</Translate>
            </span>
          </dt>
          <dd>{menuSectionEntity.description}</dd>
          <dt>
            <Translate contentKey="awesomePizzaApp.menuSection.pizzaMenu">Pizza Menu</Translate>
          </dt>
          <dd>{menuSectionEntity.pizzaMenu ? menuSectionEntity.pizzaMenu.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/menu-section" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/menu-section/${menuSectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MenuSectionDetail;
