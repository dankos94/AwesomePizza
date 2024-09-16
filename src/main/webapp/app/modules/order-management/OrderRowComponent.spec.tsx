import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import OrderRow from './OrderRowComponent';
import { IPizzaOrder } from 'app/shared/model/pizza-order.model';
import { IOrderStatus } from 'app/shared/model/order-status.model';
import { IDish } from 'app/shared/model/dish.model';

// Mock delle funzioni ausiliarie da orderHelpers
jest.mock('./orderHelpers', () => ({
  getButtonConfig: jest.fn(() => ({ enabled: true, color: 'warning' })),
  getRowStyle: jest.fn(() => 'table-warning'),
  getProgressColor: jest.fn(() => 'success'), // Mock di getProgressColor
  getProgressValue: jest.fn(() => 50), // Mock di getProgressValue
}));

describe('OrderRow Component', () => {
  // Mock dei dati
  const mockOrder: IPizzaOrder = {
    id: 1,
    dishes: [{ id: 1, name: 'Pizza' }],
    totalPrice: 25,
    orderStatus: { id: 1, statusName: 'In Progress' },
  };

  const mockOrderStatuses: IOrderStatus[] = [
    { id: 1, statusName: 'In Progress' },
    { id: 2, statusName: 'Completed' },
  ];

  const mockDishes: IDish[] = [
    { id: 1, name: 'Pizza' },
    { id: 2, name: 'Pasta' },
  ];

  const handleStatusChange = jest.fn();

  // Test: Il componente visualizza correttamente i dati dell'ordine
  test('displays order data correctly', () => {
    render(
      <table>
        <tbody>
          <OrderRow
            order={mockOrder}
            orderStatuses={mockOrderStatuses}
            dishes={mockDishes}
            isAdmin={false}
            handleStatusChange={handleStatusChange}
          />
        </tbody>
      </table>,
    );

    // Verifica che l'ID dell'ordine e il prezzo siano visualizzati
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('25 €')).toBeInTheDocument();

    // Verifica che il nome del piatto venga visualizzato
    expect(screen.getByText('Pizza')).toBeInTheDocument();

    // Verifica che lo stato dell'ordine venga visualizzato
    expect(screen.getByText('In Progress')).toBeInTheDocument();
  });

  // Test: I bottoni di cambio stato sono visibili per l'amministratore
  test('displays status change buttons when isAdmin is true', () => {
    render(
      <table>
        <tbody>
          <OrderRow
            order={mockOrder}
            orderStatuses={mockOrderStatuses}
            dishes={mockDishes}
            isAdmin={true}
            handleStatusChange={handleStatusChange}
          />
        </tbody>
      </table>,
    );

    // Verifica che i bottoni per cambiare lo stato siano visualizzati
    expect(screen.getByText('Completed')).toBeInTheDocument();
  });

  // Test: Il callback di cambio stato viene chiamato correttamente
  test('calls handleStatusChange when status button is clicked', () => {
    render(
      <table>
        <tbody>
          <OrderRow
            order={mockOrder}
            orderStatuses={mockOrderStatuses}
            dishes={mockDishes}
            isAdmin={true}
            handleStatusChange={handleStatusChange}
          />
        </tbody>
      </table>,
    );

    // Simula il click sul bottone di cambio stato
    fireEvent.click(screen.getByText('Completed'));

    // Verifica che il callback venga chiamato
    expect(handleStatusChange).toHaveBeenCalledWith(mockOrder, mockOrderStatuses[1]);
  });

  // Test: Il componente visualizza correttamente la barra di progresso per non amministratori
  test('displays progress bar when isAdmin is false', () => {
    render(
      <table>
        <tbody>
          <OrderRow
            order={mockOrder}
            orderStatuses={mockOrderStatuses}
            dishes={mockDishes}
            isAdmin={false}
            handleStatusChange={handleStatusChange}
          />
        </tbody>
      </table>,
    );

    // Verifica che il componente Progress venga visualizzato
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });
});
