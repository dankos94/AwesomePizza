import React, { useState, useEffect } from 'react';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IDish } from 'app/shared/model/dish.model';
import { getEntities as fetchPaymentMethods } from 'app/entities/payment-method/payment-method.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

interface CartSidebarProps {
  cart: IDish[];
  removeFromCart: (dishName: string) => void;
  finalizeOrder: (paymentMethod: IPaymentMethod) => void;
}

const CartSidebar: React.FC<CartSidebarProps> = ({ cart, removeFromCart, finalizeOrder }) => {
  const dispatch = useAppDispatch();
  const paymentMethods = useAppSelector(state => state?.paymentMethod?.entities);
  const [selectedPaymentMethodId, setSelectedPaymentMethodId] = useState<number | null>(null);

  // Carica i metodi di pagamento dal backend
  useEffect(() => {
    dispatch(fetchPaymentMethods({ sort: '' }));
  }, []);

  const handleFinalizeOrder = () => {
    const selectedPaymentMethod = paymentMethods.find(method => method.id === selectedPaymentMethodId);
    if (selectedPaymentMethod) {
      finalizeOrder(selectedPaymentMethod);
    } else {
      alert('Seleziona un metodo di pagamento.');
    }
  };

  return (
    <div className="cart-sidebar p-3 bg-light">
      <h2>Carrello</h2>
      {cart.length === 0 ? (
        <p>Il carrello è vuoto</p>
      ) : (
        <ul className="list-group">
          {cart.map((dish, index) => (
            <li key={index} className="list-group-item d-flex justify-content-between align-items-center">
              <div className="d-flex flex-grow-1 justify-content-between align-items-center">
                <strong className="me-3">{dish.name}</strong>
                <span className="me-3">${dish.price}</span>
              </div>
              <button onClick={() => removeFromCart(dish.name)} className="btn btn-danger btn-sm">
                Rimuovi
              </button>
            </li>
          ))}
        </ul>
      )}
      {cart.length > 0 && (
        <div className="mt-3">
          <p className="h5">Totale: {cart.reduce((total, dish) => total + dish.price, 0)} $</p>
          <label htmlFor="payment-method" className="form-label">
            {'Metodo di pagamento'}
          </label>
          <select
            id="payment-method"
            className="form-select"
            value={selectedPaymentMethodId || ''}
            onChange={e => setSelectedPaymentMethodId(Number(e.target.value))}
          >
            <option value="">Seleziona</option>
            {paymentMethods?.map(method => (
              <option key={method.id} value={method.id}>
                {method.methodName}
              </option>
            ))}
          </select>
          <button onClick={handleFinalizeOrder} className="btn btn-primary mt-3">
            {"Finalizza l'ordine"}
          </button>
        </div>
      )}
    </div>
  );
};

export default CartSidebar;
