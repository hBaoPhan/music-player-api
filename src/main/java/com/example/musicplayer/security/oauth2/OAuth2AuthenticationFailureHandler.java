package com.example.musicplayer.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.frontend.oauth2.redirect.url}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String errorCode = "oauth2_authentication_failed";
        String errorMessage = exception.getLocalizedMessage();

        if (exception instanceof OAuth2AuthenticationException oauth2Exception
                && oauth2Exception.getError() != null) {
            if (oauth2Exception.getError().getErrorCode() != null) {
                errorCode = oauth2Exception.getError().getErrorCode();
            }
            if (oauth2Exception.getError().getDescription() != null) {
                errorMessage = oauth2Exception.getError().getDescription();
            }
        }

        String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUrl.replace("/oauth2/redirect", "/login"))
                .queryParam("code", errorCode)
                .queryParam("message", errorMessage)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
