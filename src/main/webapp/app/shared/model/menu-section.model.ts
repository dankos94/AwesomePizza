import { IPizzaMenu } from 'app/shared/model/pizza-menu.model';

export interface IMenuSection {
  id?: number;
  name?: string;
  description?: string | null;
  pizzaMenu?: IPizzaMenu | null;
}

export const defaultValue: Readonly<IMenuSection> = {};
