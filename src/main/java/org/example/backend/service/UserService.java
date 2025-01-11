package org.example.backend.service;
import org.example.backend.dto.RegisterRequest;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.example.backend.model.Role;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public User register(String name, String email, String password, Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }


        // First user in the system becomes an ADMIN
        boolean isFirstUser = userRepository.count() == 0;

        // Determine role
        Role finalRole;
        if (isFirstUser) {
            finalRole = Role.ADMIN;  // First user is always admin
        } else if (role != null) {
            finalRole = role;        // Use specified role if provided
        } else {
            finalRole = Role.USER;   // Default to USER if no role specified
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(finalRole);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())  // Use email as username
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Failed to delete user with this Id");
        }
    }
    public User register(RegisterRequest registerRequest) {
        // Check if the email is already taken
        if (existsByEmail(registerRequest.getEmail())) {
            throw new ResourceNotFoundException("Email is already taken");
        }

        // Create a new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Encrypt the password
        user.setRole(registerRequest.getRole()); // Set the role (default to USER if not provided)

        // Save the user to the database
        return userRepository.save(user);
    }

}
