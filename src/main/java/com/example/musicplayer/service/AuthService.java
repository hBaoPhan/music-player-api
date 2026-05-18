package com.example.musicplayer.service;

import com.example.musicplayer.dto.*;
import com.example.musicplayer.entity.Role;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.repository.UserRepository;
import com.example.musicplayer.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) throws Exception {
        String usernameOrEmail = loginRequest.getUsername();
        String resolvedUsername = usernameOrEmail;
        User user;

        if (usernameOrEmail.contains("@")) {
            user = userRepository.findByEmail(usernameOrEmail);
            if (user == null) {
                return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy tài khoản với email này!");
            }
            resolvedUsername = user.getUsername();
        } else {
            user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        }

        if (user != null && !user.isActive()) {
            return ResponseEntity.status(403).body(Map.of(
                    "code", "account_locked",
                    "message", "Tài khoản đã bị khóa. Vui lòng liên hệ hỗ trợ."));
        }

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        resolvedUsername,
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateAccessTokenFromUsername(authentication.getName());
        String refreshToken = refreshTokenService.createRefreshToken(resolvedUsername);
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken));
    }

    public ResponseEntity<?> registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Lỗi: Username đã tồn tại!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Lỗi: Email đã được sử dụng!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok("Đăng ký tài khoản thành công!");
    }

    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy tài khoản với email này!");
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();
        Random rnd = new Random();
        while (tempPassword.length() < 8) {
            tempPassword.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        user.setPassword(passwordEncoder.encode(tempPassword.toString()));
        userRepository.save(user);

        try {
            emailService.sendTemporaryPassword(user.getEmail(), tempPassword.toString());
            return ResponseEntity.ok("Mật khẩu mới đã được gửi đến email của bạn.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi hệ thống: Không thể gửi email (kiểm tra cấu hình SMTP).");
        }
    }

    public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy người dùng!");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Lỗi: Mật khẩu cũ không chính xác!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Security: Log out from all devices on password change
        refreshTokenService.deleteAllTokensForUser(user.getUsername());

        // Generate new tokens for the current session
        String jwt = tokenProvider.generateAccessTokenFromUsername(user.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken));
    }

    public ResponseEntity<?> refreshToken(RefreshTokenRequest request) {
        String username = request.getUsername();
        String requestRefreshToken = request.getRefreshToken();

        if (username != null && requestRefreshToken != null && refreshTokenService.validateRefreshToken(username, requestRefreshToken)) {
            // Re-fetch user to ensure permissions are up to date
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null || !user.isActive()) {
                return ResponseEntity.status(403).body("Lỗi: Người dùng không tồn tại hoặc đã bị khóa!");
            }

            String newAccessToken = tokenProvider.generateAccessTokenFromUsername(username);
            return ResponseEntity.ok(new JwtResponse(newAccessToken, requestRefreshToken));
        }
        return ResponseEntity.status(403).body("Lỗi: Refresh Token không hợp lệ hoặc đã hết hạn!");
    }

    public ResponseEntity<?> logout(LogoutRequest request) {
        if (request.isAllDevices()) {
            refreshTokenService.deleteAllTokensForUser(request.getUsername());
        } else {
            refreshTokenService.deleteSpecificToken(request.getUsername(), request.getRefreshToken());
        }
        return ResponseEntity.ok("Đăng xuất thành công!");
    }
}
