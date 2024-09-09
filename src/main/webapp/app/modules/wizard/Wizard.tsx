import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getMenuSections } from 'app/entities/menu-section/menu-section.reducer';
import { getEntities as getDishes } from 'app/entities/dish/dish.reducer';
import { createEntity as createPayment, updateEntity as updatePayment } from 'app/entities/payment/payment.reducer';
import { createEntity as createOrder } from 'app/entities/pizza-order/pizza-order.reducer';
import { IDish } from 'app/shared/model/dish.model';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import CartSidebar from './CartSidebar';
import { Modal, ModalBody, ModalHeader, Spinner } from 'reactstrap';
import dayjs from 'dayjs';
import { IPizzaOrder } from 'app/shared/model/pizza-order.model';

const Wizard: React.FC = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const menuSections = useAppSelector(state => state.menuSection.entities);
  const dishes = useAppSelector(state => state.dish.entities);

  const [cart, setCart] = useState<IDish[]>([]);
  const [paymentStatus, setPaymentStatus] = useState(0);
  const [showPaymentModal, setShowPaymentModal] = useState(false);

  const addToCart = (dish: IDish) => {
    setCart([...cart, dish]);
  };

  const removeFromCart = (dishName: string) => {
    setCart(cart.filter(dish => dish.name !== dishName));
  };

  useEffect(() => {
    dispatch(getMenuSections({}));
    dispatch(getDishes({}));
  }, [dispatch]);

  const finalizeOrder = (paymentMethod: IPaymentMethod) => {
    setPaymentStatus(1);
    setShowPaymentModal(true);

    const paymentData = {
      paymentMethod,
      amount: cart.reduce((total, dish) => total + dish.price, 0),
      paymentDate: dayjs(),
      paymentStatus: { id: 1, statusName: 'In Corso' },
    };

    dispatch(createPayment(paymentData)).then(paymentResponse => {
      // @ts-expect-error tre
      const paymentId = paymentResponse.payload.data.id;

      setTimeout(() => {
        const acceptedPaymentData = {
          id: paymentId,
          ...paymentData,
          paymentDate: dayjs(),
          paymentStatus: { id: 2, statusName: 'Accettato' },
        };

        dispatch(updatePayment(acceptedPaymentData)).then(() => {
          const orderData: IPizzaOrder = {
            orderDate: dayjs(),
            totalPrice: paymentData.amount,
            payment: { id: paymentId },
            dishes: cart,
            user: account,
            orderStatus: { id: 1, statusName: 'Ricevuto' },
          };

          dispatch(createOrder(orderData)).then(() => {
            setPaymentStatus(2);
            setTimeout(() => {
              setShowPaymentModal(false);
              alert('Ordine completato con successo!');
              setCart([]);
              setPaymentStatus(0);
            }, 2000);
          });
        });
      }, 2000);
    });
  };

  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-md-8">
          <h1>Menu</h1>
          {menuSections.map(section => (
            <div key={section.id} className="mb-4">
              <h2>{section.name}</h2>
              <ul className="list-group">
                {dishes
                  .filter(dish => dish.menuSection?.id === section.id)
                  .map(dish => (
                    <li key={dish.id} className="list-group-item d-flex justify-content-between align-items-center">
                      <div className="d-flex flex-grow-1 justify-content-between align-items-center">
                        <strong className="me-3">{dish.name}</strong>
                        <span className="me-3">€{dish.price}</span>
                      </div>
                      <button onClick={() => addToCart(dish)} className="btn btn-success btn-sm">
                        Aggiungi
                      </button>
                    </li>
                  ))}
              </ul>
            </div>
          ))}
        </div>

        <div className="col-md-4">
          <CartSidebar cart={cart} removeFromCart={removeFromCart} finalizeOrder={finalizeOrder} />
        </div>
      </div>

      <Modal show={showPaymentModal} onHide={() => setShowPaymentModal(false)} centered>
        <ModalHeader closeButton>{'Stato del pagamento'}</ModalHeader>
        <ModalBody>
          {paymentStatus === 1 && (
            <div className="d-flex justify-content-center align-items-center">
              <Spinner animation="border" role="status">
                <span className="visually-hidden">{'Pagamento in corso'}</span>
              </Spinner>
              <p className="ms-3">{'Pagamento in corso...'}</p>
            </div>
          )}
          {paymentStatus === 2 && <p>{"Pagamento accettato! L'ordine è stato completato."}</p>}
        </ModalBody>
      </Modal>
    </div>
  );
};

export default Wizard;
