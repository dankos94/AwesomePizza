import React from 'react';
import { IPizzaOrder } from 'app/shared/model/pizza-order.model';
import { IOrderStatus } from 'app/shared/model/order-status.model';
import { IDish } from 'app/shared/model/dish.model';
import { Button } from 'reactstrap';
import ProgressComponent from './ProgressComponent';
import { getButtonConfig, getRowStyle } from './orderHelpers';

interface OrderRowProps {
  order: IPizzaOrder;
  orderStatuses: IOrderStatus[];
  dishes: IDish[];
  isAdmin: boolean;
  handleStatusChange: (order: IPizzaOrder, status: IOrderStatus) => void;
}

const OrderRow: React.FC<OrderRowProps> = ({ order, orderStatuses, dishes, isAdmin, handleStatusChange }) => {
  return (
    <tr key={order.id} className={getRowStyle(order.orderStatus.id, isAdmin)}>
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
          <ProgressComponent orderStatusId={order.orderStatus.id} />
        </td>
      )}
    </tr>
  );
};

export default OrderRow;
