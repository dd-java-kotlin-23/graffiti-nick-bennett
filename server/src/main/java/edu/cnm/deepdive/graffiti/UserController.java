package edu.cnm.deepdive.graffiti;

import edu.cnm.deepdive.graffiti.controller.UserApi;
import edu.cnm.deepdive.graffiti.model.dto.PrivateUserProfileDto;
import edu.cnm.deepdive.graffiti.model.dto.PublicUserProfileDto;
import edu.cnm.deepdive.graffiti.model.dto.UserProfileUpdateDto;
import edu.cnm.deepdive.graffiti.model.entity.User;
import edu.cnm.deepdive.graffiti.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController implements UserApi {

  private final UserService service;

  @Autowired
  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public User get() {
    return service.getCurrentUser();
  }

  @Override
  public @NonNull ResponseEntity<PrivateUserProfileDto> getMyUserProfile() {
    return ResponseEntity.ok(service.getUserProfile());
  }

  @Override
  @NonNull
  public ResponseEntity<PublicUserProfileDto> getUserProfile(@NonNull String key) {
    return ResponseEntity.ok(service.getUser(key));
  }

  @Override
  @NonNull
  public ResponseEntity<PrivateUserProfileDto> updateMyUserProfile(
      @NonNull UserProfileUpdateDto userProfileUpdateDto) {
    return null;
  }

}
