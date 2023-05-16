package com.example.devicemanager.exception;

import lombok.Getter;

@Getter
public class DeviceOrderException extends RuntimeException {
    public DeviceOrderException(final String message) {
        super(message);
    }
}