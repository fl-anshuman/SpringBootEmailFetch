package SpringBootEmail.ParticularEmailFetcher.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "emailsSpringBoot")
public class Email {

    @Id
    private String id;
    private String messageId;
    private String sender;
    private String subject;
    private Date date;
    private String category;
    private List<Attachment> attachments;

    @Data
    public static class Attachment {
        private String filename;
        private String mimeType;
        private String path;
    }
}
// Path: src/main/java/SpringBootEmail/ParticularEmailFetcher/service/EmailService.java