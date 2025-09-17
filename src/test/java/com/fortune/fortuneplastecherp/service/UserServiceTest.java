package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.User;
import com.fortune.fortuneplastecherp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSetupTotpSecret() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User updated = userService.setupTotpSecret(user);
        assertNotNull(updated.getTotpSecret());
    }

    @Test
    void testVerifyTotpCode() {
        User user = new User();
        String secret = com.fortune.fortuneplastecherp.util.TotpUtil.generateSecret();
        user.setTotpSecret(secret);
        String code = new org.jboss.aerogear.security.otp.Totp(secret).now();
        assertTrue(userService.verifyTotpCode(user, code));
        assertFalse(userService.verifyTotpCode(user, "000000"));
    }

    @Test
    void testDisable2fa() {
        User user = new User();
        user.setTwoFactorEnabled(true);
        user.setTotpSecret("sometotpsecret");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User updated = userService.disable2fa(user);
        assertFalse(updated.isTwoFactorEnabled());
        assertNull(updated.getTotpSecret());
    }
}
