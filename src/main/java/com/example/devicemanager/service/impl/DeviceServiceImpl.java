package com.example.devicemanager.service.impl;

import com.example.devicemanager.common.DeviceStatus;
import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.Order;
import com.example.devicemanager.domain.User;
import com.example.devicemanager.dto.OrderRequestDTO;
import com.example.devicemanager.dto.OrderResponseDTO;
import com.example.devicemanager.exception.DeviceOrderException;
import com.example.devicemanager.exception.NotFoundException;
import com.example.devicemanager.repository.DeviceRepository;
import com.example.devicemanager.service.DeviceService;
import com.example.devicemanager.service.OrderService;
import com.example.devicemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.example.devicemanager.common.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserService userService;
    private final OrderService orderService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderResponseDTO takeDevice(OrderRequestDTO requestDTO) {
        log.debug("Enter method DeviceService.takeDevice with requestDTO={}", requestDTO);
        final Device device = deviceRepository.findById(requestDTO.getDeviceId())
                .orElseThrow(() -> new NotFoundException(DEVICE_NOT_FOUND_MESSAGE));
        checkDeviceStatus(requestDTO, device);
        User user = userService.getUserById(requestDTO.getUserId());
        device.setStatus(DeviceStatus.BOOKED);
        final Order order = buildOrder(device, user);
        try {
            deviceRepository.save(device);
            orderService.saveOrder(order);
            log.debug("Finished method DeviceService.takeDevice");
            return successResponseBuilder();
        } catch (OptimisticLockException ex) {
            Order currentOrder = orderService.getActiveOrderByDeviceId(requestDTO.getDeviceId());
            throw new DeviceOrderException(String.format(DEVICE_ALREADY_BOOKED_MESSAGE, currentOrder.getUser().getName()));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderResponseDTO releaseDevice(Integer deviceId) {
        log.debug("Enter method DeviceService.returnDevice with deviceId={}", deviceId);
        final Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException(DEVICE_NOT_FOUND_MESSAGE));
        device.setStatus(DeviceStatus.AVAILABLE);
        return OrderResponseDTO.builder()
                .message(DEVICE_WAS_SUCCESSFULLY_RELEASED_MESSAGE)
                .build();
    }

    private void checkDeviceStatus(OrderRequestDTO requestDTO, Device device) {
        if (DeviceStatus.BOOKED.equals(device.getStatus())) {
            Order currentOrder = orderService.getActiveOrderByDeviceId(requestDTO.getDeviceId());
            throw new DeviceOrderException(String.format(DEVICE_ALREADY_BOOKED_MESSAGE, currentOrder.getUser().getName()));
        }
    }

    private static OrderResponseDTO successResponseBuilder() {
        return OrderResponseDTO.builder()
                .message(DEVICE_WAS_SUCCESSFULLY_BOOKED_MESSAGE)
                .build();
    }
    private static Order buildOrder(Device device, User user) {
        return Order.builder()
                .user(user)
                .device(device)
                .orderDate(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
