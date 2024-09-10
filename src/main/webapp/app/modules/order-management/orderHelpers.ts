export const getButtonConfig = (statusId: number, buttonId: number) => {
  switch (statusId) {
    case 1:
      return buttonId === 0 || buttonId === 1 ? { enabled: true, color: 'warning' } : { enabled: false, color: 'secondary' };
    case 2:
      if (buttonId === 0) return { enabled: false, color: 'success' };
      if (buttonId === 1) return { enabled: false, color: 'danger' };
      if (buttonId === 2) return { enabled: true, color: 'warning' };
      return { enabled: false, color: 'secondary' };
    case 3:
      return { enabled: false, color: 'secondary' };
    case 4:
      return buttonId === 2
        ? { enabled: false, color: 'success' }
        : { enabled: buttonId === 3, color: buttonId === 3 ? 'warning' : 'secondary' };
    case 5:
      return buttonId === 3
        ? { enabled: false, color: 'success' }
        : { enabled: buttonId === 4, color: buttonId === 4 ? 'warning' : 'secondary' };
    case 6:
      return buttonId === 4
        ? { enabled: false, color: 'success' }
        : { enabled: buttonId === 5, color: buttonId === 5 ? 'warning' : 'secondary' };
    case 7:
      return buttonId === 5
        ? { enabled: false, color: 'success' }
        : { enabled: buttonId === 6, color: buttonId === 6 ? 'warning' : 'secondary' };
    case 8:
      return { enabled: false, color: 'success' };
    default:
      return { enabled: false, color: 'secondary' };
  }
};

export const getRowStyle = (currentStatusId: number, isAdmin: boolean): string => {
  if (currentStatusId === 3) return 'table-danger';
  if (currentStatusId === 8) return 'table-success';
  if (currentStatusId !== 8 && !isAdmin) return 'table-warning';
  return '';
};

export const getProgressColor = (statusId: number) => {
  switch (statusId) {
    case 3:
      return 'danger';
    case 8:
      return 'success';
    default:
      return 'warning';
  }
};

export const getProgressValue = (orderStatusId: number) => {
  return orderStatusId === 3 ? 8 : orderStatusId;
};
