package edu.cnm.deepdive.graffiti.viewmodel;

import android.app.Activity;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.graffiti.model.domain.User;
import edu.cnm.deepdive.graffiti.service.UserService;
import jakarta.inject.Inject;

@HiltViewModel
public class UserViewModel extends ViewModel {

  private static final String TAG = UserViewModel.class.getSimpleName();

  private final UserService userService;
  private final MutableLiveData<User> user = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();

  @Inject
  UserViewModel(UserService userService) {
    this.userService = userService;
  }

  public void signIn(Activity activity) {
    error.setValue(null);
    userService
        .signIn(activity)
        .whenComplete(this::handleResult);
  }

  public void signOut() {
    error.setValue(null);
    userService
        .signOut()
        .whenComplete((_, error) -> {
          if (error != null) {
            postError(error);
          }
          user.postValue(null);
        });
  }

  private void handleResult(User user, Throwable error) {
    if (error == null) {
      this.user.postValue(user);
    } else {
      postError(error);
    }
  }

  private void postError(Throwable error) {
    Log.e(TAG, error.getMessage(), error);
    this.error.postValue(error);
  }
}
