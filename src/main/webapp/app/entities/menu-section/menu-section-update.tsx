import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPizzaMenus } from 'app/entities/pizza-menu/pizza-menu.reducer';
import { createEntity, getEntity, reset, updateEntity } from './menu-section.reducer';

export const MenuSectionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pizzaMenus = useAppSelector(state => state.pizzaMenu.entities);
  const menuSectionEntity = useAppSelector(state => state.menuSection.entity);
  const loading = useAppSelector(state => state.menuSection.loading);
  const updating = useAppSelector(state => state.menuSection.updating);
  const updateSuccess = useAppSelector(state => state.menuSection.updateSuccess);

  const handleClose = () => {
    navigate('/menu-section');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPizzaMenus({}));
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

    const entity = {
      ...menuSectionEntity,
      ...values,
      pizzaMenu: pizzaMenus.find(it => it.id.toString() === values.pizzaMenu?.toString()),
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
          ...menuSectionEntity,
          pizzaMenu: menuSectionEntity?.pizzaMenu?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="awesomePizzaApp.menuSection.home.createOrEditLabel" data-cy="MenuSectionCreateUpdateHeading">
            <Translate contentKey="awesomePizzaApp.menuSection.home.createOrEditLabel">Create or edit a MenuSection</Translate>
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
                  id="menu-section-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('awesomePizzaApp.menuSection.name')}
                id="menu-section-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('awesomePizzaApp.menuSection.description')}
                id="menu-section-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="menu-section-pizzaMenu"
                name="pizzaMenu"
                data-cy="pizzaMenu"
                label={translate('awesomePizzaApp.menuSection.pizzaMenu')}
                type="select"
              >
                <option value="" key="0" />
                {pizzaMenus
                  ? pizzaMenus.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/menu-section" replace color="info">
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

export default MenuSectionUpdate;
