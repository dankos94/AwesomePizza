import { useState } from 'react';
import { IDish } from 'app/shared/model/dish.model';

export const useCart = () => {
  const [cart, setCart] = useState<IDish[]>([]);

  const addToCart = (dish: IDish) => {
    setCart([...cart, dish]);
  };

  const removeFromCart = (dishName: string) => {
    setCart(cart.filter(dish => dish.name !== dishName));
  };

  const calculateTotal = () => {
    return cart.reduce((total, dish) => total + dish.price, 0);
  };

  return {
    cart,
    addToCart,
    removeFromCart,
    calculateTotal,
    setCart,
  };
};
