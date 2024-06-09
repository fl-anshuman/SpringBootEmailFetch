package SpringBootEmail.ParticularEmailFetcher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class CallbackController {

    private static final String EMAIL_FETCH_URL = "http://localhost:8080/api/emails";

    @GetMapping("/Callback")
    public String handleOAuthCallback() {
        // After OAuth callback is processed, trigger the email fetching
        fetchEmails();
        // Redirect to a confirmation page or home page
        return "redirect:/emails-fetched";
    }

    private void fetchEmails() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(EMAIL_FETCH_URL, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
