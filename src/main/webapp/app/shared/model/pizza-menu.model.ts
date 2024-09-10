import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IPizzaMenu {
  id?: number;
  name?: string;
  description?: string | null;
  createdDate?: dayjs.Dayjs;
  user?: IUser | null;
}

export const defaultValue: Readonly<IPizzaMenu> = {};
