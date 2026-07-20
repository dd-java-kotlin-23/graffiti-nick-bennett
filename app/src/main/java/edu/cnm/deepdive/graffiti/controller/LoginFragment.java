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
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.graffiti.R;
import edu.cnm.deepdive.graffiti.databinding.FragmentLoginBinding;
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
    binding.signIn.setOnClickListener((_) -> viewModel.signInAutomatically(requireActivity()));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getUser()
        .observe(owner, (user) -> {
          if (user != null) {
            // TODO: 7/20/26 Navigate to main fragment.
          }
        });
    viewModel
        .getError()
        .observe(owner, (error) -> {
          if (error != null) {
            binding.signIn.setEnabled(true);
            binding.signIn.setVisibility(View.VISIBLE);
            Snackbar.make(binding.getRoot(), R.string.sign_in_failure_message, Snackbar.LENGTH_LONG).show();
          }
        });
    viewModel.signInAutomatically(requireActivity());
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
