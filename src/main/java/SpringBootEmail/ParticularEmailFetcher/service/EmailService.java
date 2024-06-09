package SpringBootEmail.ParticularEmailFetcher.service;

import SpringBootEmail.ParticularEmailFetcher.model.Email;
import SpringBootEmail.ParticularEmailFetcher.repository.EmailRepository;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.client.util.Base64;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final Gmail gmailService;
    private final EmailRepository emailRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public List<Email> fetchAndSaveEmails() throws Exception {
        String user = "me";
        List<Message> messages = gmailService.users().messages().list(user).setMaxResults(30L).execute().getMessages();
        for (Message message : messages) {
            Message msg = gmailService.users().messages().get(user, message.getId()).execute();
            Email email = processMessage(msg);
            logger.info("Saving email: {}", email);
            emailRepository.save(email);
        }
        return emailRepository.findAll();
    }

    private Email processMessage(Message message) throws Exception {
        Email email = new Email();
        email.setMessageId(message.getId());

        Message fullMessage = gmailService.users().messages().get("me", message.getId()).execute();

        if (fullMessage.getPayload() != null) {
            if (fullMessage.getPayload().getHeaders() != null) {
                fullMessage.getPayload().getHeaders().forEach(header -> {
                    switch (header.getName()) {
                        case "From":
                            email.setSender(header.getValue());
                            break;
                        case "Subject":
                            email.setSubject(header.getValue());
                            break;
                        case "Date":
                            email.setDate(new Date(header.getValue()));
                            break;
                    }
                });
            }

            // Process attachments
            if (fullMessage.getPayload().getParts() != null) {
                List<Email.Attachment> attachments = new ArrayList<>();
                for (MessagePart part : fullMessage.getPayload().getParts()) {
                    if (part.getFilename() != null && !part.getFilename().isEmpty()) {
                        Email.Attachment attachment = new Email.Attachment();
                        attachment.setFilename(part.getFilename());
                        attachment.setMimeType(part.getMimeType());

                        MessagePartBody body = part.getBody();
                        byte[] fileBytes = Base64.decodeBase64(body.getData());

                        String path = "attachments/" + part.getFilename();
                        saveToFile(path, fileBytes);
                        attachment.setPath(path);

                        attachments.add(attachment);
                        logger.info("Processed attachment: {}", attachment);
                    }
                }
                email.setAttachments(attachments);
            }
        }
        return email;
    }

    private void saveToFile(String path, byte[] data) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs(); // Ensure directories exist
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }
}
