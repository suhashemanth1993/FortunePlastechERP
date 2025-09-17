package com.fortune.fortuneplastecherp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortune.fortuneplastecherp.domain.User;
import com.fortune.fortuneplastecherp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void test2faSetupAndVerify() throws Exception {
        // Register user
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "username", "testuser",
                        "email", "test@example.com",
                        "phone", "1234567890",
                        "password", "password"
                )))).andExpect(status().isOk());

        // Setup 2FA
        var setupResult = mockMvc.perform(post("/api/auth/2fa/setup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "identifier", "testuser"
                )))).andExpect(status().isOk())
                .andReturn();
        var setupJson = objectMapper.readTree(setupResult.getResponse().getContentAsString());
        String secret = setupJson.get("secret").asText();

        // Generate valid TOTP code using backend util
        String code = com.fortune.fortuneplastecherp.util.TotpUtil
                .verifyCode(secret, "000000") ? "000000" : new org.jboss.aerogear.security.otp.Totp(secret).now();

        // Verify 2FA
        mockMvc.perform(post("/api/auth/2fa/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "identifier", "testuser",
                        "code", code
                )))).andExpect(status().isOk())
                .andExpect(jsonPath("$.verified").value(true));
    }

    @Test
    void test2faDisable() throws Exception {
        // Register user
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "username", "testuser2",
                        "email", "test2@example.com",
                        "phone", "1234567891",
                        "password", "password"
                )))).andExpect(status().isOk());

        // Setup 2FA
        var setupResult = mockMvc.perform(post("/api/auth/2fa/setup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "identifier", "testuser2"
                )))).andExpect(status().isOk())
                .andReturn();
        var setupJson = objectMapper.readTree(setupResult.getResponse().getContentAsString());
        String secret = setupJson.get("secret").asText();
        String code = new org.jboss.aerogear.security.otp.Totp(secret).now();
        // Verify 2FA
        mockMvc.perform(post("/api/auth/2fa/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "identifier", "testuser2",
                        "code", code
                )))).andExpect(status().isOk())
                .andExpect(jsonPath("$.verified").value(true));
        // Disable 2FA
        mockMvc.perform(post("/api/auth/2fa/disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "identifier", "testuser2",
                        "code", code
                )))).andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));
    }
}
