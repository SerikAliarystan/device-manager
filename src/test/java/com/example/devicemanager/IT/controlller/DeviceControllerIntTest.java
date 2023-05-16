package com.example.devicemanager.IT.controlller;

import com.example.devicemanager.IT.BaseIntTest;
import com.example.devicemanager.common.Constants;
import com.example.devicemanager.common.DeviceStatus;
import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.Order;
import com.example.devicemanager.dto.OrderRequestDTO;
import com.example.devicemanager.repository.DeviceRepository;
import com.example.devicemanager.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeviceControllerIntTest extends BaseIntTest {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Should take device with status 200")
    @SqlGroup({
            @Sql(value = "classpath:sql/rollback.sql", executionPhase = AFTER_TEST_METHOD)
    })
    void shouldTakeDevice() throws Exception {
        //given
        final OrderRequestDTO requestDTO = new OrderRequestDTO(1, 1);
        final String request = objectMapper.writeValueAsString(requestDTO);
        //when
        mockMvc.perform(post("/api/v1/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo(Constants.DEVICE_WAS_SUCCESSFULLY_BOOKED_MESSAGE)));
        //then
        final Optional<Device> deviceOptional = deviceRepository.findById(1);
        assertThat(deviceOptional).isPresent().map(Device::getStatus).get().isEqualTo(DeviceStatus.BOOKED);

        Optional<Order> orderOptional = orderRepository.findTopByDeviceIdOrderByOrderDateDesc(1);
        assertThat(orderOptional).isPresent();
        final Order order = orderOptional.get();
        assertThat(order.getDevice()).isEqualTo(deviceOptional.get());
        assertThat(order.getUser().getId()).isEqualTo(1);
        assertThat(order.getOrderDate()).isBeforeOrEqualTo(LocalDateTime.now(ZoneOffset.UTC));
    }


    @Test
    @DisplayName("Should return status 409 on take device")
    @SqlGroup({
            @Sql(value = "classpath:sql/init-data.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:sql/rollback.sql", executionPhase = AFTER_TEST_METHOD)
    })
    void shouldReturnConflictOnTakeDevice() throws Exception {
        //given
        final OrderRequestDTO requestDTO = new OrderRequestDTO(1, 1);
        final String request = objectMapper.writeValueAsString(requestDTO);
        //then
        mockMvc.perform(post("/api/v1/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", equalTo(String.format(Constants.DEVICE_ALREADY_BOOKED_MESSAGE, "Captain America"))));
    }

    @Test
    @DisplayName("Should release device with status 200")
    @SqlGroup({
            @Sql(value = "classpath:sql/init-data.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:sql/rollback.sql", executionPhase = AFTER_TEST_METHOD)
    })
    void shouldReleaseDevice() throws Exception {
        //given
        final Integer deviceId = 1;
        //when
        mockMvc.perform(delete("/api/v1/device/" + deviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo(Constants.DEVICE_WAS_SUCCESSFULLY_RELEASED_MESSAGE)));
        //then
        final Optional<Device> deviceOptional = deviceRepository.findById(1);
        assertThat(deviceOptional).isPresent().map(Device::getId).get().isEqualTo(deviceId);
    }

}
