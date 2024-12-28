package org.example.backend.security;

import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to authenticate user: " + email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                System.out.println("User not found: " + email);
                return new UsernameNotFoundException("User not found with email: " + email);
            });

        System.out.println("User found: " + user.getEmail());
        System.out.println("User role: " + user.getRole());
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole().name())
            .build();
            
        System.out.println("UserDetails created successfully");
        return userDetails;
    }
} 