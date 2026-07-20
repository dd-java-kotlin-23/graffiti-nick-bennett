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
    binding.signIn.setOnClickListener((_) -> viewModel.signIn(requireActivity()));
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
            Snackbar.make().show();
          }
        });
  }

  @Override
  public void onDestroyView() {
    // TODO: 7/20/26 Release references to UI views by setting binding reference to null.
    super.onDestroyView();
  }

}
