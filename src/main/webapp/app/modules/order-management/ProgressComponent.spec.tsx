import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import ProgressComponent from './ProgressComponent';
import { getProgressColor, getProgressValue } from './orderHelpers';

// Mock delle funzioni ausiliarie
jest.mock('./orderHelpers', () => ({
  getProgressColor: jest.fn(() => 'success'),
  getProgressValue: jest.fn(() => 50),
}));

describe('ProgressComponent', () => {
  // Test: Il componente viene renderizzato correttamente
  test('renders progress bar correctly', () => {
    render(<ProgressComponent orderStatusId={1} />);

    // Verifica che la barra di progresso sia presente
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  // Test: Il colore della barra di progresso cambia in base allo stato
  test('displays correct progress bar color based on order status', () => {
    // Mock della funzione per restituire un colore diverso in base allo stato
    jest.mocked(getProgressColor).mockReturnValueOnce('danger');

    render(<ProgressComponent orderStatusId={3} />);

    // Verifica che il colore della barra sia 'danger' (ad esempio, ordine annullato)
    const progressBar = screen.getByRole('progressbar');
    expect(progressBar).toHaveClass('bg-danger');
  });

  // Test: La barra di progresso mostra il valore corretto in base allo stato
  test('displays correct progress value based on order status', () => {
    // Mock della funzione per restituire un valore specifico della barra di progresso
    jest.mocked(getProgressValue).mockReturnValueOnce(80);

    render(<ProgressComponent orderStatusId={2} />);

    // Verifica che l'attributo "aria-valuenow" sia impostato correttamente (usato per l'accessibilità)
    const progressBar = screen.getByRole('progressbar');
    expect(progressBar).toHaveAttribute('aria-valuenow', '80');
  });
});
