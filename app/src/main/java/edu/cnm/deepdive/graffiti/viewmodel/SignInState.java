package edu.cnm.deepdive.graffiti.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.graffiti.model.domain.User;

public final class SignInState {

  public enum Status {
    CHECKING_SESSION,
    SIGNED_OUT,
    SIGNING_IN,
    SIGNED_IN,
    CANCELLED,
    ERROR
  }

  private final Status status;
  private final User user;
  private final Throwable error;

  private SignInState(@NonNull Status status, @Nullable User user, @Nullable Throwable error) {
    this.status = status;
    this.user = user;
    this.error = error;
  }

  public static SignInState checkingSession() {
    return new SignInState(Status.CHECKING_SESSION, null, null);
  }

  public static SignInState signedOut() {
    return new SignInState(Status.SIGNED_OUT, null, null);
  }

  public static SignInState signingIn() {
    return new SignInState(Status.SIGNING_IN, null, null);
  }

  public static SignInState signedIn(@NonNull User user) {
    return new SignInState(Status.SIGNED_IN, user, null);
  }

  public static SignInState cancelled() {
    return new SignInState(Status.CANCELLED, null, null);
  }

  public static SignInState error(@NonNull Throwable error) {
    return new SignInState(Status.ERROR, null, error);
  }

  @NonNull
  public Status getStatus() {
    return status;
  }

  @Nullable
  public User getUser() {
    return user;
  }

  @Nullable
  public Throwable getError() {
    return error;
  }
}
