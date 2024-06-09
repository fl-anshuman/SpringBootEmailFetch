


package SpringBootEmail.ParticularEmailFetcher.repository;
import SpringBootEmail.ParticularEmailFetcher.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends MongoRepository<Email, String> {

}
