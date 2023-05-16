package com.example.devicemanager.IT.repository;

import com.example.devicemanager.IT.BaseIntTest;
import com.example.devicemanager.domain.Order;
import com.example.devicemanager.repository.OrderRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderRepositoryIntTest extends BaseIntTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Should last active order")
    @SqlGroup({
            @Sql(value = "classpath:sql/init-data.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:sql/rollback.sql", executionPhase = AFTER_TEST_METHOD)
    })
    void shouldReturnLastActiveOrder() {
        //given
        final Integer deviceId = 1;
        final Integer activeOrderId = 3;
        //when
        final Order order = orderRepository.findTopByDeviceIdOrderByOrderDateDesc(deviceId).get();
        //then
        Assertions.assertEquals(order.getId(), activeOrderId);
    }
}
