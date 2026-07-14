package edu.cnm.deepdive.graffiti.repository;

import edu.cnm.deepdive.graffiti.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByOauthKey(String oauthKey);

  Optional<User> findByExternalKey(UUID externalKey);

}
