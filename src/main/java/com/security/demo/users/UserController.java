package com.security.demo.users;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.security.demo.auth.AuthResponse;
import com.security.demo.security.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserService userService;

  // Create or Update User
  @PreAuthorize("hasAuthority('admin')")
  @PostMapping
  public ResponseEntity<User> saveUser(@RequestBody User user) {
    User savedUser = userService.saveUser(user);
    return ResponseEntity.ok(savedUser);
  }

  // Get All Users
  @PreAuthorize("isAuthenticated()")
  @GetMapping()
  public ResponseEntity<Page<User>> getUsers(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String role,
      @RequestParam(required = false) String sorted,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(userService.getUsers(username, role, page, size, sorted));
  }

  // Get User by ID
  @PreAuthorize("hasAuthority('admin')")
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.findUserById(id);
    return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  // Delete User by ID
  @PreAuthorize("hasAuthority('admin')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
    Optional<User> user = userService.findUserById(id);
    if (user.isPresent()) {
      userService.deleteUserById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/check-role")
  public ResponseEntity<List<String>> checkRole() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return ResponseEntity.ok(authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList());
  }

  // Endpoint untuk Logout (simulasi logout)
  @PostMapping("/logout")
  public ResponseEntity<AuthResponse> logout(@RequestHeader("Authorization") String token) {
    String username = jwtUtil.getUsernameFromToken(token);

    System.out.println("Username: " + username);

    if (userService.findByUsername(username).isPresent()) {
      String jwtToken = token.replace("Bearer ", "");

      if (jwtUtil.validateToken(jwtToken, username)) {
        jwtUtil.invalidateToken(jwtToken);

        return new ResponseEntity<>(new AuthResponse("Logout successful!", null), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new AuthResponse("Invalid token!", null), HttpStatus.BAD_REQUEST);
      }
    }
    return new ResponseEntity<>(new AuthResponse("User not found!", null), HttpStatus.NOT_FOUND);
  }
}
