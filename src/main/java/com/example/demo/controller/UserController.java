package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map; // ✅ Tambahkan ini

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;  

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

@GetMapping("/profile")
public ResponseEntity<Object> getUserProfile(@RequestHeader("Authorization") String token) {
    try {
        // 🔹 Ambil token tanpa "Bearer "
        String jwt = token.replace("Bearer ", "");

        // 🔹 Validasi token terlebih dahulu
        boolean isValid = jwtUtil.validateToken(jwt);
        if (!isValid) {
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Token tidak valid!"));
        }

        // 🔹 Ambil username dari token
        String username = jwtUtil.extractUsername(jwt);

        // 🔹 Cari user berdasarkan username
        return userService.findByUsername(username)
                .<ResponseEntity<Object>>map(user -> ResponseEntity.ok().body(user)) 
                .orElseGet(() -> ResponseEntity.status(404).body(Collections.singletonMap("message", "User tidak ditemukan!")));

    } catch (Exception e) {
        return ResponseEntity.status(500).body(Collections.singletonMap("error", "Terjadi kesalahan: " + e.getMessage()));
    }
}

}
