package edu.cnm.deepdive.graffiti.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.graffiti.R;
import edu.cnm.deepdive.graffiti.databinding.FragmentLoginBinding;
import edu.cnm.deepdive.graffiti.viewmodel.SignInState;
import edu.cnm.deepdive.graffiti.viewmodel.SignInState.Status;
import edu.cnm.deepdive.graffiti.viewmodel.UserViewModel;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

  private FragmentLoginBinding binding;
  private UserViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentLoginBinding.inflate(inflater, container, false);
    binding.signIn.setOnClickListener((_) -> viewModel.signInInteractively(requireActivity()));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel.getState().observe(owner, this::handleState);
    viewModel.signInAutomatically(requireActivity());
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private void handleState(SignInState state) {
    boolean busy = (state.status() == Status.CHECKING_SESSION)
        || (state.status() == Status.SIGNING_IN);
    binding.waitingIndicator.setVisibility(busy ? View.VISIBLE : View.GONE);
    binding.signIn.setVisibility(
        (busy || state.status() == Status.SIGNED_IN)
            ? View.GONE
            : View.VISIBLE
    );
    binding.signIn.setEnabled(!busy);
    if (state.status() == Status.SIGNED_IN) {
      Navigation.findNavController(binding.getRoot())
          .navigate(edu.cnm.deepdive.graffiti.controller.LoginFragmentDirections.showMainFragment());
    } else if (state.status() == Status.ERROR) {
      Snackbar.make(binding.getRoot(), R.string.sign_in_failure_message, Snackbar.LENGTH_LONG)
          .show();
    }
  }

}
