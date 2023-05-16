package com.example.devicemanager.repository;

import com.example.devicemanager.domain.Device;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface DeviceRepository extends CrudRepository<Device, Integer> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Device> findById(Integer deviceId);
}
