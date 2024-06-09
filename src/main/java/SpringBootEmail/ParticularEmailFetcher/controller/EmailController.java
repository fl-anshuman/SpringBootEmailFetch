package SpringBootEmail.ParticularEmailFetcher.controller;//package SpringBootEmail.ParticularEmailFetcher.controller;

import SpringBootEmail.ParticularEmailFetcher.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import SpringBootEmail.ParticularEmailFetcher.service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping
    public List<Email> fetchEmails() throws Exception {
        return emailService.fetchAndSaveEmails();
    }

}

