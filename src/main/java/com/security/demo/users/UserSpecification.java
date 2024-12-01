package com.security.demo.users;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UserSpecification {

    // Filter untuk username yang menggunakan LIKE
    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return null; // Tidak ada filter jika username kosong
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasRole(String role) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }
}
