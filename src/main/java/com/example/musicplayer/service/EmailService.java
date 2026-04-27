package com.example.musicplayer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    public void sendTemporaryPassword(String toEmail, String temporaryPassword) {
        String url = "https://api.brevo.com/v3/smtp/email";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("api-key", brevoApiKey);

        Map<String, Object> body = new HashMap<>();
        
        Map<String, String> sender = new HashMap<>();
        sender.put("name", "SPOTIFOUR");
        sender.put("email", "baophan2929@gmail.com");
        body.put("sender", sender);

        Map<String, String> to = new HashMap<>();
        to.put("email", toEmail);
        body.put("to", List.of(to));

        body.put("subject", "Mật khẩu tạm thời cho tài khoản SPOTIFOUR của bạn");
        body.put("textContent", "Bạn đã yêu cầu khôi phục mật khẩu.\n\n"
                + "Mật khẩu tạm thời của bạn là: " + temporaryPassword + "\n\n"
                + "Vui lòng đăng nhập lại và thay đổi mật khẩu của bạn ngay sau khi truy cập.");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            System.out.println("Gửi mail thành công qua API: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Lỗi gửi mail qua API: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email qua API", e);
        }
    }
}
