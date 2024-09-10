import dayjs from 'dayjs';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IPaymentStatus } from 'app/shared/model/payment-status.model';

export interface IPayment {
  id?: number;
  paymentDate?: dayjs.Dayjs;
  amount?: number;
  paymentMethod?: IPaymentMethod | null;
  paymentStatus?: IPaymentStatus | null;
}

export const defaultValue: Readonly<IPayment> = {};
