package com.example.user_web_service.repository;

import com.example.user_web_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String user);

    User findByEmail(String email);
    List<User> findAllByEmailOrPhone(String email, String phone);

    List<User> findAllByRoleName(String roleName);
}