package edu.cnm.deepdive.graffiti.viewmodel;

import android.app.Activity;
import android.util.Log;
import androidx.credentials.exceptions.GetCredentialCancellationException;
import androidx.credentials.exceptions.NoCredentialException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.graffiti.model.domain.User;
import edu.cnm.deepdive.graffiti.service.UserService;
import jakarta.inject.Inject;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;

@HiltViewModel
public class UserViewModel extends ViewModel {

  private static final String TAG = UserViewModel.class.getSimpleName();

  private final UserService userService;
  private final MutableLiveData<SignInState> state =
      new MutableLiveData<>(SignInState.checkingSession());
  private final LiveData<User> user = Transformations.map(state, SignInState::user);
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();
  private final AtomicBoolean requestInProgress = new AtomicBoolean();

  @Inject
  UserViewModel(UserService userService) {
    this.userService = userService;
  }

  public LiveData<SignInState> getState() {
    return state;
  }

  public LiveData<User> getUser() {
    return user;
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  public void signInAutomatically(Activity activity) {
    error.setValue(null);
    SignInState currentState = state.getValue();
    if (currentState == null
        || currentState.status() != SignInState.Status.CHECKING_SESSION) {
      return;
    }
    if (!requestInProgress.compareAndSet(false, true)) {
      return;
    }
    userService
        .signInAutomatically(activity)
        .whenComplete(this::handleAutomaticResult);
  }

  public void signInInteractively(Activity activity) {
    if (!requestInProgress.compareAndSet(false, true)) {
      return;
    }
    state.setValue(SignInState.signingIn());
    userService
        .signInInteractively(activity)
        .whenComplete(this::handleInteractiveResult);
  }

  public void signOut() {
    error.setValue(null);
    userService
        .signOut()
        .whenComplete((_, error) -> {
          if (error != null) {
            postError(unwrap(error));
          } else {
            state.postValue(SignInState.signedOut());
          }
        });
  }

  private void handleAutomaticResult(User user, Throwable error) {
    requestInProgress.set(false);
    if (error == null) {
      state.postValue(SignInState.signedIn(user));
    } else {
      Throwable cause = unwrap(error);
      if (cause instanceof NoCredentialException
          || cause instanceof GetCredentialCancellationException) {
        state.postValue(SignInState.signedOut());
      } else {
        postError(cause);
      }
    }
  }

  private void handleInteractiveResult(User user, Throwable error) {
    requestInProgress.set(false);
    if (error == null) {
      state.postValue(SignInState.signedIn(user));
    } else {
      Throwable cause = unwrap(error);
      if (cause instanceof GetCredentialCancellationException) {
        state.postValue(SignInState.cancelled());
      } else if (cause instanceof NoCredentialException) {
        state.postValue(SignInState.signedOut());
      } else {
        postError(cause);
      }
    }
  }

  private void postError(Throwable error) {
    Log.e(TAG, error.getMessage(), error);
    this.error.postValue(error);
  }

  private Throwable unwrap(Throwable error) {
    Throwable cause = error;
    while ((cause instanceof CompletionException) && (cause.getCause() != null)) {
      cause = cause.getCause();
    }
    return cause;
  }

}
