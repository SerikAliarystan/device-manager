package com.example.devicemanager.web.handler;

import com.example.devicemanager.dto.OrderResponseDTO;
import com.example.devicemanager.exception.DeviceOrderException;
import com.example.devicemanager.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SuppressWarnings("unused")
@RestControllerAdvice
@Slf4j
public class DeviceControllerHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public OrderResponseDTO notFoundExceptionHandler(NotFoundException ex) {
        log.error("throws NotFoundException with message {}", ex.getMessage());
        return OrderResponseDTO.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {DeviceOrderException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public OrderResponseDTO orderExceptionHandler(DeviceOrderException ex) {
        log.error("throws DeviceOrderException with message {}", ex.getMessage());
        return OrderResponseDTO.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public OrderResponseDTO exceptionHandler(Exception ex) {
        log.error("unexpected error", ex);
        return OrderResponseDTO.builder()
                .message("Oops, something went wrong!!!")
                .build();
    }
}
