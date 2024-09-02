package dev.Abhishek.EmailService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WelcomeUserDto {
    private String name;
    private String email;
    private String from;
    private String body;
    private String subject;
}
