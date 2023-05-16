package com.example.devicemanager.repository;

import com.example.devicemanager.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
