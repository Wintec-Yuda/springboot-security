package com.security.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Filter setiap permintaan untuk memverifikasi token JWT
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Ambil header Authorization dari request
        String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String role = null;
        String jwtToken = null;

        // Mengecek apakah header Authorization ada dan dimulai dengan "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // Mengambil token setelah "Bearer "
            username = jwtUtil.getUsernameFromToken(jwtToken); // Mendapatkan username dari token
            role = jwtUtil.getRoleFromToken(jwtToken);
        }

        // Jika token ada dan username ditemukan
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validasi token
            if (jwtUtil.validateToken(jwtToken, username)) {
                // Ubah role menjadi GrantedAuthority
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role)); // Tambahkan role sebagai authority
                // Membuat otentikasi menggunakan username yang ditemukan
                var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Menyimpan otentikasi ke dalam SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Lanjutkan ke filter berikutnya
        chain.doFilter(request, response);
    }
}
