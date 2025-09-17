package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.User;
import com.fortune.fortuneplastecherp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.fortune.fortuneplastecherp.util.TotpUtil;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User registerUser(String username, String email, String phone, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setTwoFactorEnabled(false);
        return userRepository.save(user);
    }

    public Optional<User> findByUsernameOrEmailOrPhone(String identifier) {
        if (identifier == null) return Optional.empty();
        Optional<User> user = userRepository.findByUsername(identifier);
        if (user.isPresent()) return user;
        user = userRepository.findByEmail(identifier);
        if (user.isPresent()) return user;
        return userRepository.findByPhone(identifier);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    // Helper: Setup TOTP secret for user
    public User setupTotpSecret(User user) {
        String secret = TotpUtil.generateSecret();
        user.setTotpSecret(secret);
        return userRepository.save(user);
    }

    // Helper: Verify TOTP code
    public boolean verifyTotpCode(User user, String code) {
        if (user.getTotpSecret() == null) return false;
        return TotpUtil.verifyCode(user.getTotpSecret(), code);
    }

    // Helper: Disable 2FA
    public User disable2fa(User user) {
        user.setTwoFactorEnabled(false);
        user.setTotpSecret(null);
        return userRepository.save(user);
    }
}
