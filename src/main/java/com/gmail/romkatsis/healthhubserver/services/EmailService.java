package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.exceptions.CanNotSendMailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final String senderEmailAddress;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username}") String senderEmailAddress) {
        this.mailSender = mailSender;
        this.senderEmailAddress = senderEmailAddress;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmailAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new CanNotSendMailException("There was an error sending the email");
        }
    }
}
