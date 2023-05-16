package com.example.devicemanager.repository;

import com.example.devicemanager.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    Optional<Order> findTopByDeviceIdOrderByOrderDateDesc(Integer deviceId);
}