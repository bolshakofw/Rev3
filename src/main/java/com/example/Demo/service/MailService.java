package com.example.Demo.service;

import com.example.Demo.enums.MailEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mail;

    //todo передавать енам *
    public void sendEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mail);
        message.setTo(toEmail);
        message.setText(MailEnum.FILE_UPLOADED.getBody());
        message.setSubject(MailEnum.FILE_UPLOADED.getSubject());

        mailSender.send(message);

        log.info("Mail with subject: \"" + MailEnum.FILE_UPLOADED.getSubject() + "\" successfully sent to: " + toEmail);

    }

}
