package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.domain.User;
import com.fortune.fortuneplastecherp.service.UserService;
import com.fortune.fortuneplastecherp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import com.fortune.fortuneplastecherp.util.TotpUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String email = req.get("email");
        String phone = req.get("phone");
        String password = req.get("password");
        if ((username != null && userService.findByUsernameOrEmailOrPhone(username).isPresent()) ||
            (email != null && userService.findByUsernameOrEmailOrPhone(email).isPresent()) ||
            (phone != null && userService.findByUsernameOrEmailOrPhone(phone).isPresent())) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        User user = userService.registerUser(username, email, phone, password);
        return ResponseEntity.ok(user);
    }

    // Step 1: Begin 2FA setup (generate secret and otpauth URL)
    @PostMapping("/2fa/setup")
    public ResponseEntity<?> setup2fa(@RequestBody Map<String, String> req) {
        String identifier = req.get("identifier");
        Optional<User> userOpt = userService.findByUsernameOrEmailOrPhone(identifier);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        User user = userOpt.get();
        String secret = TotpUtil.generateSecret();
        user.setTotpSecret(secret);
        userService.save(user);
        String otpauth = TotpUtil.getOtpAuthUrl(user.getUsername() != null ? user.getUsername() : user.getEmail(), secret, "FortuneERP");
        return ResponseEntity.ok(Map.of(
            "secret", secret,
            "otpauthUrl", otpauth
        ));
    }

    // Step 2: Verify TOTP code (user enters code from authenticator app)
    @PostMapping("/2fa/verify")
    public ResponseEntity<?> verify2fa(@RequestBody Map<String, String> req) {
        String identifier = req.get("identifier");
        String code = req.get("code");
        Optional<User> userOpt = userService.findByUsernameOrEmailOrPhone(identifier);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        User user = userOpt.get();
        if (user.getTotpSecret() == null) {
            return ResponseEntity.badRequest().body("2FA not setup");
        }
        boolean valid = TotpUtil.verifyCode(user.getTotpSecret(), code);
        if (valid) {
            user.setTwoFactorEnabled(true);
            userService.save(user);
            return ResponseEntity.ok(Map.of("verified", true));
        } else {
            return ResponseEntity.status(401).body(Map.of("verified", false));
        }
    }

    // Step 3: Disable 2FA
    @PostMapping("/2fa/disable")
    public ResponseEntity<?> disable2fa(@RequestBody Map<String, String> req) {
        String identifier = req.get("identifier");
        String code = req.get("code");
        Optional<User> userOpt = userService.findByUsernameOrEmailOrPhone(identifier);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        User user = userOpt.get();
        if (!user.isTwoFactorEnabled() || user.getTotpSecret() == null) {
            return ResponseEntity.badRequest().body("2FA not enabled");
        }
        boolean valid = TotpUtil.verifyCode(user.getTotpSecret(), code);
        if (valid) {
            user.setTwoFactorEnabled(false);
            user.setTotpSecret(null);
            userService.save(user);
            return ResponseEntity.ok(Map.of("disabled", true));
        } else {
            return ResponseEntity.status(401).body(Map.of("disabled", false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String identifier = req.get("identifier"); // can be username, email, or phone
        String password = req.get("password");
        Optional<User> userOpt = userService.findByUsernameOrEmailOrPhone(identifier);
        if (userOpt.isEmpty() || !userService.checkPassword(userOpt.get(), password)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        User user = userOpt.get();
        String token = jwtUtil.generateToken(identifier);
        return ResponseEntity.ok(Map.of(
            "userId", user.getId(),
            "twoFactorEnabled", user.isTwoFactorEnabled(),
            "token", token
        ));
    }
}
