import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getMenuSections } from 'app/entities/menu-section/menu-section.reducer';
import { getEntities as getPizzaOrders } from 'app/entities/pizza-order/pizza-order.reducer';
import { createEntity, getEntity, reset, updateEntity } from './dish.reducer';

export const DishUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const menuSections = useAppSelector(state => state.menuSection.entities);
  const pizzaOrders = useAppSelector(state => state.pizzaOrder.entities);
  const dishEntity = useAppSelector(state => state.dish.entity);
  const loading = useAppSelector(state => state.dish.loading);
  const updating = useAppSelector(state => state.dish.updating);
  const updateSuccess = useAppSelector(state => state.dish.updateSuccess);

  const handleClose = () => {
    navigate('/dish');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMenuSections({}));
    dispatch(getPizzaOrders({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...dishEntity,
      ...values,
      menuSection: menuSections.find(it => it.id.toString() === values.menuSection?.toString()),
      pizzaOrders: mapIdList(values.pizzaOrders),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...dishEntity,
          menuSection: dishEntity?.menuSection?.id,
          pizzaOrders: dishEntity?.pizzaOrders?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="awesomePizzaApp.dish.home.createOrEditLabel" data-cy="DishCreateUpdateHeading">
            <Translate contentKey="awesomePizzaApp.dish.home.createOrEditLabel">Create or edit a Dish</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="dish-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('awesomePizzaApp.dish.name')}
                id="dish-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('awesomePizzaApp.dish.description')}
                id="dish-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('awesomePizzaApp.dish.price')}
                id="dish-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('awesomePizzaApp.dish.available')}
                id="dish-available"
                name="available"
                data-cy="available"
                check
                type="checkbox"
              />
              <ValidatedField
                id="dish-menuSection"
                name="menuSection"
                data-cy="menuSection"
                label={translate('awesomePizzaApp.dish.menuSection')}
                type="select"
              >
                <option value="" key="0" />
                {menuSections
                  ? menuSections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('awesomePizzaApp.dish.pizzaOrder')}
                id="dish-pizzaOrder"
                data-cy="pizzaOrder"
                type="select"
                multiple
                name="pizzaOrders"
              >
                <option value="" key="0" />
                {pizzaOrders
                  ? pizzaOrders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dish" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DishUpdate;
