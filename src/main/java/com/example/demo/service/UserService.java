package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Tambahkan metode ini untuk mendapatkan semua user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Tambahkan metode ini untuk membuat user baru
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username sudah digunakan!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password sebelum disimpan
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerUser(String username, String password) {
    if (userRepository.findByUsername(username).isPresent()) {
        throw new RuntimeException("Username sudah digunakan!");
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password)); // Hash password
    return userRepository.save(user);
}

}
