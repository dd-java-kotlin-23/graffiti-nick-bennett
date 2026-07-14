package edu.cnm.deepdive.graffiti.repository;

import edu.cnm.deepdive.graffiti.model.entity.Canvas;
import edu.cnm.deepdive.graffiti.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanvasRepository extends JpaRepository<Canvas, Long> {

  Optional<Canvas> findByExternalKey(UUID externalKey);

  List<Canvas> findAllByOwnerOrderByCreatedDesc(User owner);

}
