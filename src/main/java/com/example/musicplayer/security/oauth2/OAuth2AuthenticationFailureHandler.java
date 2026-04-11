package com.example.musicplayer.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.frontend.oauth2.redirect.url}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        System.err.println(">>> Lỗi xác thực OAuth2: " + exception.getMessage());
        exception.printStackTrace();

        // Trích xuất trang đăng nhập frontend từ redirect url (giả sử nó là localhost:5173/oauth2/redirect)
        // Chúng ta sẽ redirect về trang login của frontend kèm theo lỗi
        String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUrl.replace("/oauth2/redirect", "/login"))
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
