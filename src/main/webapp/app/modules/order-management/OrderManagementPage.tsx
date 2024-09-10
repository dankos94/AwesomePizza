import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import pizzaOrder, { getEntities as getOrders, updateEntity as updateOrder } from 'app/entities/pizza-order/pizza-order.reducer';
import orderStatus, { getEntities as getOrderStatuses } from 'app/entities/order-status/order-status.reducer';
import { getEntities as getDishes } from 'app/entities/dish/dish.reducer';
import { IPizzaOrder } from 'app/shared/model/pizza-order.model';
import { IOrderStatus } from 'app/shared/model/order-status.model';
import { Button, Table } from 'reactstrap';
import { IDish } from 'app/shared/model/dish.model';
import { AUTHORITIES } from 'app/config/constants';
import { ProgressBar } from 'react-bootstrap';

const OrderManagementPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const orders: IPizzaOrder[] = useAppSelector(state => state.pizzaOrder.entities);
  const orderStatuses: IOrderStatus[] = useAppSelector(state => state.orderStatus.entities);
  const dishes: IDish[] = useAppSelector(state => state.dish.entities);
  const authentication = useAppSelector(state => state.authentication);
  const isAdmin = authentication?.account?.authorities?.includes(AUTHORITIES.ADMIN);

  const ordersFiltered = isAdmin ? orders : orders.filter(pzzOrd => pzzOrd.user.id === authentication?.account?.id);

  const [clickedStatus, setClickedStatus] = useState<{ [key: number]: number }>({});

  useEffect(() => {
    dispatch(getOrders({}));
    dispatch(getOrderStatuses({}));
    dispatch(getDishes({}));
  }, []);

  const handleStatusChange = (order: IPizzaOrder, newStatus: IOrderStatus) => {
    const updatedOrder = {
      ...order,
      orderStatus: newStatus,
    };
    dispatch(updateOrder(updatedOrder));
    setClickedStatus(prev => ({ ...prev, [order.id]: newStatus.id }));
  };

  const getButtonConfig = (statusId: number, buttonId: number) => {
    switch (statusId) {
      case 1: // Ricevuto
        if (buttonId === 0 || buttonId === 1) return { enabled: true, color: 'warning' }; // Accettato e Rifiutato giallo
        return { enabled: false, color: 'secondary' }; // Grigio
      case 2: // Accettato
        if (buttonId === 0) return { enabled: false, color: 'success' }; // Accettato verde
        if (buttonId === 1) return { enabled: false, color: 'danger' }; // Rifiutato rosso
        if (buttonId === 2) return { enabled: true, color: 'warning' }; // Assegnato giallo
        return { enabled: false, color: 'secondary' }; // Grigio
      case 3: // Rifiutato
        return { enabled: false, color: 'secondary' }; // Disabilita tutto (grigio)
      case 4: // Assegnato
        if (buttonId === 2) return { enabled: false, color: 'success' }; // Assegnato verde
        if (buttonId === 3) return { enabled: true, color: 'warning' }; // In preparazione giallo
        return { enabled: false, color: 'secondary' }; // Grigio
      case 5: // In Preparazione
        if (buttonId === 3) return { enabled: false, color: 'success' }; // In preparazione verde
        if (buttonId === 4) return { enabled: true, color: 'warning' }; // Ritirato giallo
        return { enabled: false, color: 'secondary' }; // Grigio
      case 6: // Ritirato
        if (buttonId === 4) return { enabled: false, color: 'success' }; // Ritirato verde
        if (buttonId === 5) return { enabled: true, color: 'warning' }; // In consegna giallo
        return { enabled: false, color: 'secondary' }; // Grigio
      case 7: // In Consegna
        if (buttonId === 5) return { enabled: false, color: 'success' }; // In consegna verde
        if (buttonId === 6) return { enabled: true, color: 'warning' }; // Consegnato giallo
        return { enabled: false, color: 'secondary' }; // Grigio
      case 8: // Consegnato
        return { enabled: false, color: 'success' }; // Consegnato verde
      default:
        return { enabled: false, color: 'secondary' }; // Default grigio
    }
  };

  const getRowStyle = (currentStatusId: number): string => {
    if (currentStatusId === 3) return 'table-danger'; // Rifiutato (riga rossa)
    if (currentStatusId === 8) return 'table-success'; // Consegnato (riga verde)
    if (currentStatusId !== 8 && !isAdmin) return 'table-warning';
    return ''; // Default nessuno
  };

  const getProgressColor = (statusId: number) => {
    switch (statusId) {
      case 3:
        return 'danger'; // Rosso per rifiutato
      case 8:
        return 'success'; // Verde per consegnato
      default:
        return 'warning'; // Giallo per gli altri
    }
  };

  const getProgressValue = (orderStatusId: number) => {
    if (orderStatusId === 3) return 8;
    return orderStatusId;
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
            <tr key={order.id} className={getRowStyle(order.orderStatus.id)}>
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
              {isAdmin ? (
                <td>
                  {orderStatuses.slice(1).map((status, index) => {
                    const buttonConfig = getButtonConfig(order.orderStatus.id, index);
                    return (
                      <Button
                        key={status.id}
                        onClick={() => handleStatusChange(order, status)}
                        disabled={!buttonConfig.enabled}
                        color={buttonConfig.color}
                        className="m-1"
                      >
                        {status.statusName}
                      </Button>
                    );
                  })}
                </td>
              ) : (
                <td>
                  <ProgressBar
                    striped
                    variant={getProgressColor(order.orderStatus.id)}
                    min={1}
                    now={getProgressValue(order.orderStatus.id)}
                    max={8}
                  />
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default OrderManagementPage;
