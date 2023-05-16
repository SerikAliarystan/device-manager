package com.example.devicemanager.service;

import com.example.devicemanager.domain.Order;

public interface OrderService {
    Order getActiveOrderByDeviceId(Integer deviceId);
    void saveOrder(Order order);
}
