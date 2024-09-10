import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getOrderStatuses } from 'app/entities/order-status/order-status.reducer';
import { getEntities as getDishes } from 'app/entities/dish/dish.reducer';
import { createEntity, getEntity, reset, updateEntity } from './pizza-order.reducer';

export const PizzaOrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.payment.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const orderStatuses = useAppSelector(state => state.orderStatus.entities);
  const dishes = useAppSelector(state => state.dish.entities);
  const pizzaOrderEntity = useAppSelector(state => state.pizzaOrder.entity);
  const loading = useAppSelector(state => state.pizzaOrder.loading);
  const updating = useAppSelector(state => state.pizzaOrder.updating);
  const updateSuccess = useAppSelector(state => state.pizzaOrder.updateSuccess);

  const handleClose = () => {
    navigate('/pizza-order');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPayments({}));
    dispatch(getUsers({}));
    dispatch(getOrderStatuses({}));
    dispatch(getDishes({}));
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
    values.orderDate = convertDateTimeToServer(values.orderDate);
    if (values.totalPrice !== undefined && typeof values.totalPrice !== 'number') {
      values.totalPrice = Number(values.totalPrice);
    }

    const entity = {
      ...pizzaOrderEntity,
      ...values,
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
      orderStatus: orderStatuses.find(it => it.id.toString() === values.orderStatus?.toString()),
      dishes: mapIdList(values.dishes),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          orderDate: displayDefaultDateTime(),
        }
      : {
          ...pizzaOrderEntity,
          orderDate: convertDateTimeFromServer(pizzaOrderEntity.orderDate),
          payment: pizzaOrderEntity?.payment?.id,
          user: pizzaOrderEntity?.user?.id,
          orderStatus: pizzaOrderEntity?.orderStatus?.id,
          dishes: pizzaOrderEntity?.dishes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="awesomePizzaApp.pizzaOrder.home.createOrEditLabel" data-cy="PizzaOrderCreateUpdateHeading">
            <Translate contentKey="awesomePizzaApp.pizzaOrder.home.createOrEditLabel">Create or edit a PizzaOrder</Translate>
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
                  id="pizza-order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('awesomePizzaApp.pizzaOrder.orderDate')}
                id="pizza-order-orderDate"
                name="orderDate"
                data-cy="orderDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('awesomePizzaApp.pizzaOrder.totalPrice')}
                id="pizza-order-totalPrice"
                name="totalPrice"
                data-cy="totalPrice"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="pizza-order-payment"
                name="payment"
                data-cy="payment"
                label={translate('awesomePizzaApp.pizzaOrder.payment')}
                type="select"
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pizza-order-user"
                name="user"
                data-cy="user"
                label={translate('awesomePizzaApp.pizzaOrder.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pizza-order-orderStatus"
                name="orderStatus"
                data-cy="orderStatus"
                label={translate('awesomePizzaApp.pizzaOrder.orderStatus')}
                type="select"
              >
                <option value="" key="0" />
                {orderStatuses
                  ? orderStatuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('awesomePizzaApp.pizzaOrder.dish')}
                id="pizza-order-dish"
                data-cy="dish"
                type="select"
                multiple
                name="dishes"
              >
                <option value="" key="0" />
                {dishes
                  ? dishes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pizza-order" replace color="info">
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

export default PizzaOrderUpdate;
