import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getOrders, updateEntity as updateOrder } from 'app/entities/pizza-order/pizza-order.reducer';
import { getEntities as getOrderStatuses } from 'app/entities/order-status/order-status.reducer';
import { getEntities as getDishes } from 'app/entities/dish/dish.reducer';
import { Table } from 'reactstrap';
import OrderRow from './OrderRowComponent';

const OrderManagementPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const orders = useAppSelector(state => state.pizzaOrder.entities);
  const orderStatuses = useAppSelector(state => state.orderStatus.entities);
  const dishes = useAppSelector(state => state.dish.entities);
  const authentication = useAppSelector(state => state.authentication);
  const isAdmin = authentication?.account?.authorities?.includes('ROLE_ADMIN');

  const ordersFiltered = isAdmin ? orders : orders.filter(pzzOrd => pzzOrd.user.id === authentication?.account?.id);
  const [dataChanged, setDataChanged] = useState(false);

  useEffect(() => {
    dispatch(getOrders({}));
    dispatch(getOrderStatuses({}));
    dispatch(getDishes({}));
  }, [dispatch]);

  useEffect(() => {
    // Stabilire la connessione SSE una sola volta
    const eventSource = new EventSource('/api/pizza-orders/orders/stream');

    eventSource.onopen = function (event) {
      /* eslint-disable no-console */
      console.log('Connessione aperta, stato connessione SSE:', eventSource.readyState);
      dispatch(getOrders({}));
      setDataChanged(prevState => !prevState);
    };

    eventSource.onerror = function (err) {
      console.error('Errore con la connessione SSE:', err);
      /* eslint-disable no-console */
      console.log('Stato connessione SSE:', eventSource.readyState);
      eventSource.close();
    };
    return () => eventSource.close();
  }, [dataChanged]); // Usa un array vuoto come dipendenza per eseguire l'effetto solo al montaggio

  // Gestione del cambiamento di stato dell'ordine
  const handleStatusChange = (order, newStatus) => {
    const updatedOrder = { ...order, orderStatus: newStatus };
    dispatch(updateOrder(updatedOrder));
  };

  return (
    <div className="container">
      <h1>Gestione Ordini</h1>
      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>ID Ordine</th>
            <th>Piatti</th>
            <th>Totale</th>
            <th>Stato Attuale</th>
            {isAdmin ? <th>Azione</th> : <th>Progress</th>}
          </tr>
        </thead>
        <tbody>
          {ordersFiltered.map(order => (
            <OrderRow
              key={order.id}
              order={order}
              orderStatuses={orderStatuses}
              dishes={dishes}
              isAdmin={isAdmin}
              handleStatusChange={handleStatusChange}
            />
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default OrderManagementPage;
