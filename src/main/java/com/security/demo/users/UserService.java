package com.security.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create or Update User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Method untuk mendapatkan paginated user berdasarkan username, role, dan
    // sorting
    public Page<User> getUsers(String username, String role, int page, int size, String sorted) {

        // Membuat Pageable dengan nomor halaman, ukuran halaman dan sorting
        Pageable pageable = PageRequest.of(page, size);

        // Menggunakan Specification untuk filter berdasarkan username dan role
        Specification<User> spec = Specification
                .where(UserSpecification.hasUsername(username))
                .and(UserSpecification.hasRole(role))
                .and(UserSpecification.applySorting(sorted));

        // Mengambil data dengan pagination, filtering, dan sorting
        return userRepository.findAll(spec, pageable);
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
