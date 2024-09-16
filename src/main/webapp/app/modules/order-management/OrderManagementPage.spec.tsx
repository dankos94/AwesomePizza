import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import OrderManagementPage from './OrderManagementPage';

// Importa i tuoi reducer
import * as pizzaOrderReducer from 'app/entities/pizza-order/pizza-order.reducer';
import orderStatusReducer from 'app/entities/order-status/order-status.reducer';
import dishReducer from 'app/entities/dish/dish.reducer';

// Mock delle funzioni nel reducer
jest.mock('app/entities/pizza-order/pizza-order.reducer', () => ({
  getEntities: jest.fn(), // Stiamo facendo il mock dell'intero modulo
  updateEntity: jest.fn(),
}));

jest.mock('app/entities/order-status/order-status.reducer', () => ({
  getEntities: jest.fn(),
}));

jest.mock('app/entities/dish/dish.reducer', () => ({
  getEntities: jest.fn(),
}));

// Crea uno store mockato con Redux Toolkit
const store = configureStore({
  reducer: {
    pizzaOrder: pizzaOrderReducer.default, // Utilizza `default` se stai esportando il reducer come `default`
    orderStatus: orderStatusReducer,
    dish: dishReducer,
  },
});

describe('OrderManagementPage Component', () => {
  beforeEach(() => {
    jest.clearAllMocks(); // Pulisce i mock prima di ogni test
  });

  test('renders OrderManagementPage without crashing', () => {
    render(
      <Provider store={store}>
        <OrderManagementPage />
      </Provider>,
    );
    expect(screen.getByText('Orders')).toBeInTheDocument(); // Verifica che la pagina si carichi
  });

  test('loads and displays orders correctly', async () => {
    const mockOrders = [{ id: 1, name: 'Order 1' }];
    (pizzaOrderReducer.getEntities as unknown as jest.Mock).mockResolvedValueOnce(mockOrders); // Usa `jest.Mock`

    render(
      <Provider store={store}>
        <OrderManagementPage />
      </Provider>,
    );

    await waitFor(() => {
      expect(screen.getByText('Order 1')).toBeInTheDocument(); // Verifica che l'ordine venga visualizzato
    });
  });

  test('displays error message when order loading fails', async () => {
    (pizzaOrderReducer.getEntities as unknown as jest.Mock).mockRejectedValueOnce(new Error('Failed to load orders'));

    render(
      <Provider store={store}>
        <OrderManagementPage />
      </Provider>,
    );

    await waitFor(() => {
      expect(screen.getByText('Error loading orders')).toBeInTheDocument(); // Verifica che venga visualizzato un messaggio di errore
    });
  });
});
