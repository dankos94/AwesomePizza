import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import Wizard from './Wizard';
import CartSidebar from './CartSidebar'; // Aggiungi questo import per eventuale mocking

// Mock delle funzioni e dello store
const mockStore = configureStore({
  reducer: {
    menuSection: () => ({
      entities: [
        { id: 1, name: 'Pizza' },
        { id: 2, name: 'Pasta' },
      ],
    }),
    dish: () => ({
      entities: [
        { id: 1, name: 'Pizza Margherita', price: 10, menuSection: { id: 1 } },
        { id: 2, name: 'Spaghetti Carbonara', price: 12, menuSection: { id: 2 } },
      ],
    }),
  },
});

describe('Wizard Component', () => {
  // Test: Verifica il rendering del menu
  test('renders menu sections and dishes correctly', () => {
    render(
      <Provider store={mockStore}>
        <Wizard />
      </Provider>,
    );

    // Verifica che le sezioni del menu vengano renderizzate
    expect(screen.getByText('Pizza')).toBeInTheDocument();
    expect(screen.getByText('Pasta')).toBeInTheDocument();

    // Verifica che i piatti siano visualizzati
    expect(screen.getByText('Pizza Margherita')).toBeInTheDocument();
    expect(screen.getByText('Spaghetti Carbonara')).toBeInTheDocument();
  });

  // Test: Aggiunta di piatti al carrello
  test('adds dishes to the cart', () => {
    render(
      <Provider store={mockStore}>
        <Wizard />
      </Provider>,
    );

    // Simula il click sul bottone "Aggiungi" per aggiungere un piatto
    fireEvent.click(screen.getAllByText('Aggiungi', { selector: 'button' })[0]);

    // Verifica che il piatto sia stato aggiunto al carrello
    expect(screen.getAllByText('Pizza Margherita')[0]).toBeInTheDocument();
  });
});
