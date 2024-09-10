import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import pizzaOrder, { getEntities as getOrders, updateEntity as updateOrder } from 'app/entities/pizza-order/pizza-order.reducer';
import orderStatus, { getEntities as getOrderStatuses } from 'app/entities/order-status/order-status.reducer';
import { getEntities as getDishes } from 'app/entities/dish/dish.reducer';
import { IPizzaOrder } from 'app/shared/model/pizza-order.model';
import { IOrderStatus } from 'app/shared/model/order-status.model';
import { Button, Table } from 'reactstrap';
import { IDish } from 'app/shared/model/dish.model';

const OrderManagementPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const orders = useAppSelector(state => state.pizzaOrder.entities); // Ottiene gli ordini dallo store
  const orderStatuses: IOrderStatus[] = useAppSelector(state => state.orderStatus.entities);
  const dishes: IDish[] = useAppSelector(state => state.dish.entities);

  useEffect(() => {
    // Carica gli ordini e stati quando il componente viene montato
    dispatch(getOrders({}));
    dispatch(getOrderStatuses({}));
    dispatch(getDishes({}));
  }, [dispatch]);

  // Funzione per aggiornare lo stato di un ordine
  const handleStatusChange = (order: IPizzaOrder, newStatus: IOrderStatus) => {
    const updatedOrder = {
      ...order,
      orderStatus: newStatus,
    };
    dispatch(updateOrder(updatedOrder));
  };

  const isButtonDisabled = (currentStatusId: number, buttonStatusId: number): boolean => {
    // Logica di disabilitazione:
    // Se lo stato attuale è "Rifiutato" (ID = 3), disabilita tutti i bottoni
    if (currentStatusId === 3) return true;
    // Se lo stato attuale è "Accettato" (ID = 2) o oltre, disabilita i bottoni successivi al successivo
    if (buttonStatusId === 2 && currentStatusId >= 3) return true;
    // Se il bottone rappresenta uno stato successivo allo stato attuale, disabilita i bottoni successivi
    if (buttonStatusId > currentStatusId + 1) return true;
    // Se il bottone rappresenta uno stato precedente o uguale allo stato attuale, disabilita
    if (buttonStatusId <= currentStatusId) return true;
    // Se nessuna delle condizioni precedenti è soddisfatta, il bottone è abilitato
    return false;
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
            <th>Azione</th>
          </tr>
        </thead>
        <tbody>
          {orders.map(order => (
            <tr key={order.id}>
              <td>{order.id}</td>
              <td>
                <ul>
                  {order.dishes.map(dish => (
                    <li key={dish.id}>{dishes.find(d => d.id === dish.id)?.name}</li>
                  ))}
                </ul>
              </td>
              <td>{order.totalPrice} $</td>
              <td>{orderStatuses.find(os => os.id === order.orderStatus.id)?.statusName}</td>
              <td>
                {orderStatuses.slice(1).map(status => (
                  <Button
                    key={status.id}
                    onClick={() => handleStatusChange(order, status)}
                    disabled={isButtonDisabled(order.orderStatus.id, status.id)}
                    className="m-1"
                  >
                    {status.statusName}
                  </Button>
                ))}
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default OrderManagementPage;
