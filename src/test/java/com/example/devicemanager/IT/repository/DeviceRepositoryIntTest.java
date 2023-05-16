package com.example.devicemanager.IT.repository;

import com.example.devicemanager.IT.BaseIntTest;
import com.example.devicemanager.common.DeviceStatus;
import com.example.devicemanager.domain.Device;
import com.example.devicemanager.repository.DeviceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeviceRepositoryIntTest extends BaseIntTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    @DisplayName("Should return expected device")
    void shouldReturnDevice() {
        //given
        final Device device = Device.builder()
                .id(1)
                .name("Samsung Galaxy S9")
                .status(DeviceStatus.AVAILABLE)
                .version(0)
                .build();
        final Integer deviceId = 1;
        //when
        final Device expectedDevice = deviceRepository.findById(deviceId).get();
        //then
        Assertions.assertEquals(expectedDevice, device);
    }
}