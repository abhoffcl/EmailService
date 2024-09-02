package dev.Abhishek.EmailService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.Abhishek.EmailService.dto.WelcomeUserDto;
import dev.Abhishek.EmailService.exception.EmailSendingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailConsumer {

    private  ObjectMapper objectMapper;
    private  JavaMailSender mailSender;

    @Autowired
    public SendEmailConsumer(ObjectMapper objectMapper, JavaMailSender mailSender) {
        this.objectMapper = objectMapper;
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailServer")
    public void sendEmailHandler(String message) {
        try {
            WelcomeUserDto welcomeUserDto = objectMapper.readValue(message, WelcomeUserDto.class);
            sendEmail(welcomeUserDto);
        } catch (JsonProcessingException e) {
            throw new EmailSendingException("Error converting string to json");
        } catch (MailException e) {
            throw new EmailSendingException("Failed to send mail");
        }
    }

    public void sendEmail(WelcomeUserDto welcomeUserDto)throws MailException {
        // Create a simple email message
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(welcomeUserDto.getEmail());
        mailMessage.setFrom(welcomeUserDto.getFrom());
        mailMessage.setSubject(welcomeUserDto.getSubject());
        mailMessage.setText(welcomeUserDto.getBody());
        mailSender.send(mailMessage);
    }
}
