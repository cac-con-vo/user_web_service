package com.example.user_web_service.service;

import com.example.user_web_service.entity.Role;
import com.example.user_web_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public void addRole(String name) {
        Role newRole = new Role();
        newRole.setName(name);

        roleRepository.save(newRole);
    }

    public Role findRoleByName(String name) {
        return roleRepository.findFirstByName(name);
    }
}
