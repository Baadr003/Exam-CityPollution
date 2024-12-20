// notificationPreferences.test.js
import { render, fireEvent, screen } from '@testing-library/react';
import NotificationPreferences from './NotificationPreferences';

describe('NotificationPreferences', () => {
  const mockPreferences = {
    aqiThreshold: 100,
    emailNotificationsEnabled: true,
    appNotificationsEnabled: true
  };

  test('should display current AQI threshold', () => {
    render(
      <NotificationPreferences 
        preferences={mockPreferences}
        setPreferences={() => {}}
      />
    );
    expect(screen.getByText('AQI 100')).toBeInTheDocument();
  });

  test('should update preferences when slider changes', () => {
    const setPreferences = jest.fn();
    render(
      <NotificationPreferences 
        preferences={mockPreferences}
        setPreferences={setPreferences}
      />
    );
    
    const slider = screen.getByRole('slider');
    fireEvent.change(slider, { target: { value: 150 } });
    
    expect(setPreferences).toHaveBeenCalledWith({
      ...mockPreferences,
      aqiThreshold: 150
    });
  });
});