package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Dapatkan semua user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Tambahkan user baru dengan transaksi
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username sudah digunakan!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password sebelum simpan
        return userRepository.save(user);
    }

    // ✅ Login user
    public Optional<User> loginUser(String username, String password) {
        logger.info("Mencoba login dengan username: {}", username);
        
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            logger.warn("User {} tidak ditemukan!", username);
            throw new RuntimeException("User tidak ditemukan!");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Password salah untuk user: {}", username);
            throw new RuntimeException("Password salah!");
        }

        logger.info("User {} berhasil login", username);
        return Optional.of(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // ✅ Register user baru dengan transaksi
    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username sudah digunakan!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Hash password sebelum simpan
        return userRepository.save(user);
    }
}
