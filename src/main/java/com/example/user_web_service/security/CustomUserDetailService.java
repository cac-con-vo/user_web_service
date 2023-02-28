package com.example.user_web_service.security;

import com.example.user_web_service.entity.User;
import com.example.user_web_service.exception.UserNotFoundException;
import com.example.user_web_service.repository.UserRepository;
import com.example.user_web_service.security.userprincipal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Cacheable(value = "userDetails")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username, "not found!"));
        return Principal.build(user);
    }
}
