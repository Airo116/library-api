package com.library_api.service;

import com.library_api.model.User;
import com.library_api.repository.UserRepository;
import org.hibernate.query.sqm.tree.select.SqmDynamicInstantiation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private Map<String, String> resetTokens = new java.util.HashMap<>();
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return  org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
    public User registerNewUser(String username, String password, String email, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(username, passwordEncoder.encode(password), email, fullName);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    //временное хранение токенов


    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Пользователь с таким email не найден"));
        String token = java.util.UUID.randomUUID().toString();
        resetTokens.put(token, user.getUsername());

        //Здесь можно отправить email
        System.out.println("==============================");
        System.out.println("Ссылка для сброса пароля:");
        System.out.println("http://localhost:8080/reset-password?token=" + token);
        System.out.println("==============================");
    }

    public boolean validatePasswordResetToken(String token) {
        return resetTokens.containsKey(token);
    }

    public void resetPassword(String token, String newPassword) {
        String username = resetTokens.get(token);
        if (username == null) {
            throw new RuntimeException("Недействиетльный токен");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokens.remove(token);
    }


    //пользователь

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользовватель не найден"));
    }

    public void updateProfile(String username, String email, String fullName) {
        User user = findByUsername(username);

        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email уже используется");
        }

        user.setEmail(email);
        user.setFullName(fullName);
        userRepository.save(user);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = findByUsername(username);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Текущий пароль неверен");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }



}