import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './pizza-order.reducer';

export const PizzaOrder = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const pizzaOrderList = useAppSelector(state => state.pizzaOrder.entities);
  const loading = useAppSelector(state => state.pizzaOrder.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="pizza-order-heading" data-cy="PizzaOrderHeading">
        <Translate contentKey="awesomePizzaApp.pizzaOrder.home.title">Pizza Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="awesomePizzaApp.pizzaOrder.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pizza-order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="awesomePizzaApp.pizzaOrder.home.createLabel">Create new Pizza Order</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pizzaOrderList && pizzaOrderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="awesomePizzaApp.pizzaOrder.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('orderDate')}>
                  <Translate contentKey="awesomePizzaApp.pizzaOrder.orderDate">Order Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderDate')} />
                </th>
                <th className="hand" onClick={sort('totalPrice')}>
                  <Translate contentKey="awesomePizzaApp.pizzaOrder.totalPrice">Total Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalPrice')} />
                </th>
                <th>
                  <Translate contentKey="awesomePizzaApp.pizzaOrder.payment">Payment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="awesomePizzaApp.pizzaOrder.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="awesomePizzaApp.pizzaOrder.orderStatus">Order Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="awesomePizzaApp.pizzaOrder.dish">Dish</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pizzaOrderList.map((pizzaOrder, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pizza-order/${pizzaOrder.id}`} color="link" size="sm">
                      {pizzaOrder.id}
                    </Button>
                  </td>
                  <td>{pizzaOrder.orderDate ? <TextFormat type="date" value={pizzaOrder.orderDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{pizzaOrder.totalPrice}</td>
                  <td>{pizzaOrder.payment ? <Link to={`/payment/${pizzaOrder.payment.id}`}>{pizzaOrder.payment.id}</Link> : ''}</td>
                  <td>{pizzaOrder.user ? pizzaOrder.user.id : ''}</td>
                  <td>
                    {pizzaOrder.orderStatus ? (
                      <Link to={`/order-status/${pizzaOrder.orderStatus.id}`}>{pizzaOrder.orderStatus.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {pizzaOrder.dishes
                      ? pizzaOrder.dishes.map((val, j) => (
                          <span key={j}>
                            <Link to={`/dish/${val.id}`}>{val.id}</Link>
                            {j === pizzaOrder.dishes.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pizza-order/${pizzaOrder.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/pizza-order/${pizzaOrder.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/pizza-order/${pizzaOrder.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="awesomePizzaApp.pizzaOrder.home.notFound">No Pizza Orders found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PizzaOrder;
