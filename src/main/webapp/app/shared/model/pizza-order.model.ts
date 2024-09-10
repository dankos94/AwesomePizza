import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { IUser } from 'app/shared/model/user.model';
import { IOrderStatus } from 'app/shared/model/order-status.model';
import { IDish } from 'app/shared/model/dish.model';

export interface IPizzaOrder {
  id?: number;
  orderDate?: dayjs.Dayjs;
  totalPrice?: number;
  payment?: IPayment;
  user?: IUser;
  orderStatus?: IOrderStatus;
  dishes?: IDish[];
}

export const defaultValue: Readonly<IPizzaOrder> = {};
