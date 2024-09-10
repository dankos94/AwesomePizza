import React from 'react';
import { ProgressBar } from 'react-bootstrap';
import { getProgressColor, getProgressValue } from './orderHelpers';

interface ProgressComponentProps {
  orderStatusId: number;
}

const ProgressComponent: React.FC<ProgressComponentProps> = ({ orderStatusId }) => {
  return <ProgressBar striped variant={getProgressColor(orderStatusId)} min={1} now={getProgressValue(orderStatusId)} max={8} />;
};

export default ProgressComponent;
