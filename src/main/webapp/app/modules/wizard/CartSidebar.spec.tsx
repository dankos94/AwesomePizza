import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import CartSidebar from './CartSidebar';

// Mock delle funzioni removeFromCart e finalizeOrder
const removeFromCart = jest.fn();
const finalizeOrder = jest.fn();

// Mock del carrello
const mockCart = [
  { id: 1, name: 'Pizza Margherita', price: 10 },
  { id: 2, name: 'Spaghetti alla Carbonara', price: 12 },
];

// Mock dei metodi di pagamento
const mockPaymentMethods = [
  { id: 'credit-card', name: 'Carta di credito' },
  { id: 'paypal', name: 'PayPal' },
];

// Crea uno store di mock con il carrello e i metodi di pagamento
const store = configureStore({
  reducer: {
    cart: (state = mockCart) => state,
    paymentMethods: (state = mockPaymentMethods) => state, // Aggiungi i metodi di pagamento allo store
  },
});

describe('CartSidebar Component', () => {
  // Test: Verifica che i prodotti nel carrello siano renderizzati correttamente
  test('renders cart items correctly', () => {
    render(
      <Provider store={store}>
        <CartSidebar cart={mockCart} removeFromCart={removeFromCart} finalizeOrder={finalizeOrder} />
      </Provider>,
    );

    // Usa una RegExp per cercare il testo "10 €" e "12 €"
    expect(screen.getByText(/€\s*10/i)).toBeInTheDocument();
    expect(screen.getByText(/€\s*12/i)).toBeInTheDocument();
  });

  // Test: Verifica che la rimozione di un prodotto dal carrello funzioni correttamente
  test('calls removeFromCart when remove button is clicked', () => {
    render(
      <Provider store={store}>
        <CartSidebar cart={mockCart} removeFromCart={removeFromCart} finalizeOrder={finalizeOrder} />
      </Provider>,
    );

    // Simula il click sul bottone di rimozione del primo prodotto
    fireEvent.click(screen.getAllByText('Rimuovi')[0]);

    // Verifica che la funzione di rimozione venga chiamata con il nome del prodotto corretto
    expect(removeFromCart).toHaveBeenCalledWith('Pizza Margherita');
  });
});
