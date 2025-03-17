package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


import java.util.Optional; // 🔹 Tambahkan import ini!

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok("User " + newUser.getUsername() + " berhasil didaftarkan!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //   @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
    //     try {
    //         Optional<User> user = userService.loginUser(username, password);
    //         return ResponseEntity.ok(user.get()); // ✅ Berhasil login
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage()); // ❌ Gagal login
    //     }
    // }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
    String username = request.get("username");
    String password = request.get("password");

    System.out.println("Login attempt: " + username + " - " + password);

    try {
        Optional<User> user = userService.loginUser(username, password);
        return ResponseEntity.ok(user.get()); // ✅ Berhasil login
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage()); // ❌ Gagal login
    }
}
}
