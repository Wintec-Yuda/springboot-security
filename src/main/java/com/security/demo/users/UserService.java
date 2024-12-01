package com.security.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create or Update User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Find All Users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Find User by ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Find User by Username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Delete User by ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // Delete User by Entity
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
