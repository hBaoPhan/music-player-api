package com.example.musicplayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTemporaryPassword(String toEmail, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("baophan2929@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Mật khẩu tạm thời cho tài khoản SPOTIFOUR của bạn");
        message.setText("Bạn đã yêu cầu khôi phục mật khẩu.\n\n"
                + "Mật khẩu tạm thời của bạn là: " + temporaryPassword + "\n\n"
                + "Vui lòng đăng nhập lại và thay đổi mật khẩu của bạn ngay sau khi truy cập.");

        mailSender.send(message);
    }
}
