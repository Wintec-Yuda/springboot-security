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

    // Filter untuk role
    public static Specification<User> hasRole(String role) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    // Sorting berdasarkan atribut dengan penanda '-' untuk descending
    public static Specification<User> applySorting(String sortBy) {
        return (root, query, criteriaBuilder) -> {
            if (sortBy == null || sortBy.isEmpty()) {
                return null; // Tidak ada sorting jika sortBy kosong
            }
            boolean isDescending = sortBy.startsWith("-");
            String attribute = isDescending ? sortBy.substring(1) : sortBy; // Hilangkan '-' jika ada
            
            if (isDescending) {
                query.orderBy(criteriaBuilder.desc(root.get(attribute)));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(attribute)));
            }
            
            return null; // Specification tidak perlu kondisi filter untuk sorting
        };
    }
}
