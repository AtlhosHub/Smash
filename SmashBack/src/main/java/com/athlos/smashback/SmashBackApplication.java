package com.athlos.smashback;

import com.athlos.smashback.service.EmailReaderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmashBackApplication implements CommandLineRunner {
    private final EmailReaderService emailReaderService;
    public SmashBackApplication(EmailReaderService emailReaderService) {
        this.emailReaderService = emailReaderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SmashBackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        emailReaderService.verificarEmails();
    }
}
