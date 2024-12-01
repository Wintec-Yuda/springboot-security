package com.security.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  // Access restricted to admin users only
  @PreAuthorize("hasAuthority('admin')")
  @GetMapping("/admin")
  public String getAdminData() {
    return "This is restricted to ADMIN users.";
  }

  // Access restricted to authenticated users
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/user")
  public String getUserData() {
    return "This is accessible to authenticated users.";
  }

  // Endpoint untuk Logout (simulasi logout)
  @PostMapping("/logout")
  public ResponseEntity<AuthResponse> logout(@RequestParam String username,
      @RequestHeader("Authorization") String token) {
    if (userService.findByUsername(username).isPresent()) {
      // Invalidate the token (blacklist it or add it to a logout list)
      String jwtToken = token.replace("Bearer ", ""); // Remove "Bearer " if present

      if (jwtUtil.validateToken(jwtToken, username)) {
        // Add the token to the blacklist
        jwtUtil.invalidateToken(jwtToken);

        // Optionally, you can clear cookies or session if necessary on the client side

        return new ResponseEntity<>(new AuthResponse("Logout successful!", null), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new AuthResponse("Invalid token!", null), HttpStatus.BAD_REQUEST);
      }
    }
    return new ResponseEntity<>(new AuthResponse("User not found!", null), HttpStatus.NOT_FOUND);
  }
}
