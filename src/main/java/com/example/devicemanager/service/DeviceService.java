package com.example.devicemanager.service;

import com.example.devicemanager.dto.OrderRequestDTO;
import com.example.devicemanager.dto.OrderResponseDTO;

public interface DeviceService {

    OrderResponseDTO takeDevice(OrderRequestDTO requestDTO);
    OrderResponseDTO releaseDevice(Integer deviceId);
}
