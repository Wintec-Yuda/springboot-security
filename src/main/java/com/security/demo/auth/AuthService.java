package com.security.demo.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.demo.security.JwtUtil;
import com.security.demo.users.User;
import com.security.demo.users.UserRepository;

@Service
public class AuthService {
  @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

  // Register User
    public User registerUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));  // Hashing password
        user.setRole(role);
        return userRepository.save(user);
    }

    // Login User (verifikasi username dan password, kemudian generate token JWT)
    public String loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            // Generate JWT token jika login berhasil
            return jwtUtil.generateToken(username, user.get().getRole());
        }
        return null;  // Invalid credentials
    }
}
