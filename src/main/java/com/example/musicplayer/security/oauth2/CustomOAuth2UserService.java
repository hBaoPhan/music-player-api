package com.example.musicplayer.security.oauth2;

import com.example.musicplayer.entity.AuthProvider;
import com.example.musicplayer.entity.CustomUserDetails;
import com.example.musicplayer.entity.Role;
import com.example.musicplayer.entity.User;
import com.example.musicplayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email không tồn tại từ OAuth2");
        }


        User user = userRepository.findByEmail(email);

        if (user != null && !user.isActive()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("account_locked", "Tài khoản đã bị khóa", null));
        }
        
        if (user != null) {
            if (user.getProvider() != AuthProvider.GOOGLE) {
                user.setProvider(AuthProvider.GOOGLE);
                userRepository.save(user);
            }
        } else {
            user = new User();
            user.setEmail(email);

            String baseUsername = email.split("@")[0];
            String uniqueUsername = baseUsername;
            int counter = 1;
            while (userRepository.existsByUsername(uniqueUsername)) {
                uniqueUsername = baseUsername + counter;
                counter++;
            }
            user.setUsername(uniqueUsername);
            user.setProvider(AuthProvider.GOOGLE);
            user.setRole(Role.USER);
            user = userRepository.save(user);
        }

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
}
