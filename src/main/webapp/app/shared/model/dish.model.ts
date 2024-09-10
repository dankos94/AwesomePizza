import { IMenuSection } from 'app/shared/model/menu-section.model';
import { IPizzaOrder } from 'app/shared/model/pizza-order.model';

export interface IDish {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  available?: boolean;
  menuSection?: IMenuSection | null;
  pizzaOrders?: IPizzaOrder[] | null;
}

export const defaultValue: Readonly<IDish> = {
  available: false,
};
