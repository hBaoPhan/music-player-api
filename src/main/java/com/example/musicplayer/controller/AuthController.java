package com.example.musicplayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musicplayer.dto.JwtResponse;
import com.example.musicplayer.dto.LoginRequest;
import com.example.musicplayer.dto.RegisterRequest;
import com.example.musicplayer.dto.ForgotPasswordRequest;
import com.example.musicplayer.dto.ChangePasswordRequest;
import com.example.musicplayer.entity.Role;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.repository.UserRepository;
import com.example.musicplayer.security.JwtTokenProvider;
import com.example.musicplayer.service.EmailService;
import com.example.musicplayer.service.UserService;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

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
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {

        String usernameOrEmail = loginRequest.getUsername();
        String resolvedUsername = usernameOrEmail;

        if (usernameOrEmail != null && usernameOrEmail.contains("@")) {
            User user = userRepository.findByEmail(usernameOrEmail);
            if (user == null) {
                return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy tài khoản với email này!");
            }
            resolvedUsername = user.getUsername();
        }

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        resolvedUsername,
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Nếu tài khoản đã bị vô hiệu hóa trước đó, tự động kích hoạt lại
        User loggedInUser = userRepository.findByUsername(resolvedUsername).orElse(null);
        boolean wasReactivated = false;
        if (loggedInUser != null && !loggedInUser.isActive()) {
            userService.reactivateUser(loggedInUser.getId());
            wasReactivated = true;
        }

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt, wasReactivated));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest signUpRequest) {
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().trim().isEmpty() ||
                signUpRequest.getPassword() == null || signUpRequest.getPassword().trim().isEmpty() ||
                signUpRequest.getEmail() == null || signUpRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Lỗi: Không được để trống thông tin đăng ký!");
        }

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

        userRepository.save(user);

        return ResponseEntity.ok("Đăng ký tài khoản thành công!");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Lỗi: Email không được để trống!");
        }

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
            System.err.println("Lỗi gửi mail: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Lỗi hệ thống: Không thể gửi email (kiểm tra cấu hình SMTP).");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        if (request.getUsername() == null || request.getOldPassword() == null || request.getNewPassword() == null) {
            return ResponseEntity.badRequest().body("Lỗi: Thiếu thông tin đổi mật khẩu!");
        }

        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy người dùng!");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Lỗi: Mật khẩu cũ không chính xác!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }
}
