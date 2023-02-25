package com.example.user_web_service;

import com.example.user_web_service.entity.Role;
import com.example.user_web_service.entity.User;
import com.example.user_web_service.entity.UserStatus;
import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.repository.UserRepository;
import com.example.user_web_service.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class UserWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWebServiceApplication.class, args);
    }



    @Autowired
    RoleService roleService;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void initializeRole() {
        Role roleUser = roleService.findRoleByName(Constant.USER_ROLE);
        if (roleUser == null) {
            System.out.println("Role user not found");
            roleService.addRole(Constant.USER_ROLE);
        }
        Role roleAdmin = roleService.findRoleByName(Constant.ADMIN_ROLE);
        if (roleAdmin == null) {
            System.out.println("Role admin not found");
            roleService.addRole(Constant.ADMIN_ROLE);
        }

        User firstAdmin = userRepository.findByEmail("admin@gmail.com");

        if (firstAdmin == null) {
            firstAdmin = new User();
            roleAdmin = roleService.findRoleByName(Constant.ADMIN_ROLE);

            firstAdmin.setRole(roleAdmin);
            firstAdmin.setUsername("admin");
            firstAdmin.setEmail("admin@gmail.com");
            firstAdmin.setFirstName(" Admin");
            firstAdmin.setLastName(" Page");
            firstAdmin.setPhone("0906679963");
            firstAdmin.setPassword(new BCryptPasswordEncoder().encode("admin123456"));

            firstAdmin.setCreateAt(Constant.getCurrentDateTime());
            firstAdmin.setUpdateAt(null);
            firstAdmin.setStatus(UserStatus.ACTIVE);
            userRepository.save(firstAdmin);
        }
    }
}
