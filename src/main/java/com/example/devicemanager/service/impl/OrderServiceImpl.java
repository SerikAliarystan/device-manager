package com.example.devicemanager.service.impl;

import com.example.devicemanager.domain.Order;
import com.example.devicemanager.exception.NotFoundException;
import com.example.devicemanager.repository.OrderRepository;
import com.example.devicemanager.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.devicemanager.common.Constants.ORDER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Order getActiveOrderByDeviceId(Integer deviceId) {
        return orderRepository
                .findTopByDeviceIdOrderByOrderDateDesc(deviceId)
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
