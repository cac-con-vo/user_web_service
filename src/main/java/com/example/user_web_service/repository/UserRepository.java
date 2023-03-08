package com.example.user_web_service.repository;

import com.example.user_web_service.entity.User;
import com.example.user_web_service.security.userprincipal.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String user);

    User findByEmail(String email);
    List<User> findAllByEmailOrPhone(String email, String phone);

    List<User> findAllByRoleName(String roleName);
    User getByUsername(String username);

//    Optional<User> findByEmailGoogle(String email);
//
//    Boolean existsByEmail(String email);

}