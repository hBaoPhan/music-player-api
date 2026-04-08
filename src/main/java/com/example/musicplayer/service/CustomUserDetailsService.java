package com.example.musicplayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.musicplayer.entity.CustomUserDetails;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

@Override
public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    System.out.println(">>> Đang tìm user trong DB với username/email: " + usernameOrEmail);

    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + usernameOrEmail));

    System.out.println(">>> Đã tìm thấy user: " + user.getUsername() + ", Role: " + user.getRole());

    return new CustomUserDetails(user);
}
}
