package com.example.devicemanager.service;

import com.example.devicemanager.common.DeviceStatus;
import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.Order;
import com.example.devicemanager.domain.User;
import com.example.devicemanager.dto.OrderRequestDTO;
import com.example.devicemanager.dto.OrderResponseDTO;
import com.example.devicemanager.exception.DeviceOrderException;
import com.example.devicemanager.exception.NotFoundException;
import com.example.devicemanager.repository.DeviceRepository;
import com.example.devicemanager.service.impl.DeviceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.devicemanager.common.Constants.DEVICE_WAS_SUCCESSFULLY_BOOKED_MESSAGE;
import static com.example.devicemanager.common.Constants.DEVICE_WAS_SUCCESSFULLY_RELEASED_MESSAGE;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    private static final String USER_NAME = "Tony Stark";
    private static final String DEVICE_NAME = "Iphone 14";
    @Mock
    private OrderService orderService;
    @Mock
    private UserService userService;
    @Mock
    private DeviceRepository deviceRepository;
    @InjectMocks
    private DeviceServiceImpl deviceService;

    @Test
    @DisplayName("Should return OrderResponseDTO when Device is successfully booked")
    void whenDeviceSuccessfullyBookedThenReturnDto() {
        //given
        final OrderRequestDTO requestDTO = OrderRequestDTO.builder().deviceId(1).userId(1).build();
        final Device device = Device.builder().id(1).name(DEVICE_NAME).status(DeviceStatus.AVAILABLE).build();
        final User user = User.builder().id(1).name(USER_NAME).build();
        //when
        Mockito.when(deviceRepository.findById(requestDTO.getDeviceId()))
                .thenReturn(Optional.of(device));
        Mockito.when(userService.getUserById(requestDTO.getUserId()))
                .thenReturn(user);
        Mockito.when(deviceRepository.save(ArgumentMatchers.any()))
                .thenReturn(device);
        Mockito.doNothing().when(orderService).saveOrder(ArgumentMatchers.any());
        //then
        final OrderResponseDTO responseDTO = deviceService.takeDevice(requestDTO);
        Assertions.assertEquals(responseDTO.getMessage(), DEVICE_WAS_SUCCESSFULLY_BOOKED_MESSAGE);
    }

    @Test
    @DisplayName("Should throw an exception when Device is not found by id")
    void whenDeviceNotFoundThenException() {
        //given
        final OrderRequestDTO requestDTO = OrderRequestDTO.builder().deviceId(1).userId(1).build();
        //when
        Mockito.when(deviceRepository.findById(requestDTO.getDeviceId()))
                .thenReturn(Optional.empty());
        //then
        Assertions.assertThrows(NotFoundException.class, () -> deviceService.takeDevice(requestDTO));
    }

    @Test
    @DisplayName("Should throw an exception when User is not found by id")
    void whenUserNotFoundThenException() {
        //given
        final OrderRequestDTO requestDTO = OrderRequestDTO.builder().deviceId(1).userId(1).build();
        final Device device = Device.builder().id(1).name(DEVICE_NAME).status(DeviceStatus.AVAILABLE).build();
        //when
        Mockito.when(deviceRepository.findById(requestDTO.getDeviceId()))
                .thenReturn(Optional.of(device));
        Mockito.when(userService.getUserById(requestDTO.getUserId()))
                .thenThrow(NotFoundException.class);
        //then
        Assertions.assertThrows(NotFoundException.class, () -> deviceService.takeDevice(requestDTO));
    }

    @Test
    @DisplayName("Should throw an exception when Order is not found by id")
    void whenOrderNotFoundThenException() {
        //given
        final OrderRequestDTO requestDTO = OrderRequestDTO.builder().deviceId(1).userId(1).build();
        final Device device = Device.builder().id(1).name(DEVICE_NAME).status(DeviceStatus.BOOKED).build();
        //when
        Mockito.when(deviceRepository.findById(requestDTO.getDeviceId()))
                .thenReturn(Optional.of(device));
        Mockito.when(orderService.getActiveOrderByDeviceId(requestDTO.getDeviceId()))
                .thenThrow(NotFoundException.class);
        //then
        Assertions.assertThrows(NotFoundException.class, () -> deviceService.takeDevice(requestDTO));
    }

    @Test
    @DisplayName("Should throw an exception when Device already booked")
    void whenDeviceIsBookedThenException() {
        //given
        final OrderRequestDTO requestDTO = OrderRequestDTO.builder().deviceId(1).userId(1).build();
        final Device device = Device.builder().id(1).name(DEVICE_NAME).status(DeviceStatus.BOOKED).build();
        final User user = User.builder().id(1).name(USER_NAME).build();
        final Order order = Order.builder().id(1).device(device).user(user).orderDate(LocalDateTime.now()).build();
        //when
        Mockito.when(deviceRepository.findById(requestDTO.getDeviceId()))
                .thenReturn(Optional.of(device));
        Mockito.when(orderService.getActiveOrderByDeviceId(requestDTO.getDeviceId()))
                .thenReturn(order);
        //then
        Assertions.assertThrows(DeviceOrderException.class, () -> deviceService.takeDevice(requestDTO));
    }

    @Test
    @DisplayName("Should throw an exception when optimistic locking occurs")
    void whenOptimisticLockThenException() {
        //given
        final OrderRequestDTO requestDTO = OrderRequestDTO.builder().deviceId(1).userId(1).build();
        final Device device = Device.builder().id(1).name(DEVICE_NAME).status(DeviceStatus.AVAILABLE).build();
        final User user = User.builder().id(1).name(USER_NAME).build();
        final Order order = Order.builder().id(1).device(device).user(user).orderDate(LocalDateTime.now()).build();
        //when
        Mockito.when(deviceRepository.findById(requestDTO.getDeviceId()))
                .thenReturn(Optional.of(device));
        Mockito.when(userService.getUserById(requestDTO.getUserId()))
                .thenReturn(user);
        Mockito.when(orderService.getActiveOrderByDeviceId(requestDTO.getDeviceId()))
                .thenReturn(order);
        Mockito.when(deviceRepository.save(ArgumentMatchers.any()))
                .thenThrow(OptimisticLockException.class);
        //then
        Assertions.assertThrows(DeviceOrderException.class, () -> deviceService.takeDevice(requestDTO));
    }

    @Test
    @DisplayName("Should return OrderResponseDTO when Device is successfully released")
    void whenDeviceIsReleasedThenReturnDto() {
        //given
        final Device device = Device.builder().id(1).name(DEVICE_NAME).status(DeviceStatus.BOOKED).build();
        //when
        Mockito.when(deviceRepository.findById(device.getId()))
                .thenReturn(Optional.of(device));
        //then
        final OrderResponseDTO responseDTO = deviceService.releaseDevice(device.getId());
        Assertions.assertEquals(responseDTO.getMessage(), DEVICE_WAS_SUCCESSFULLY_RELEASED_MESSAGE);
    }

    @Test
    @DisplayName("Should throw an exception when a device is not found when releasing the device")
    void whenDeviceNotFoundWhileReleaseThenException() {
        //given
        final Device device = Device.builder().id(1).name(DEVICE_NAME).status(DeviceStatus.BOOKED).build();
        //when
        Mockito.when(deviceRepository.findById(device.getId()))
                .thenReturn(Optional.empty());
        //then
        Assertions.assertThrows(NotFoundException.class, () -> deviceService.releaseDevice(device.getId()));
    }

}
