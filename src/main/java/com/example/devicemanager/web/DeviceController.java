package com.example.devicemanager.web;

import com.example.devicemanager.dto.OrderRequestDTO;
import com.example.devicemanager.dto.OrderResponseDTO;
import com.example.devicemanager.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/device")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public OrderResponseDTO takeDevice(@RequestBody final OrderRequestDTO request) {
        return deviceService.takeDevice(request);
    }

    @DeleteMapping("/{deviceId}")
    public OrderResponseDTO releaseDevice(@PathVariable final Integer deviceId) {
        return deviceService.releaseDevice(deviceId);
    }
}
