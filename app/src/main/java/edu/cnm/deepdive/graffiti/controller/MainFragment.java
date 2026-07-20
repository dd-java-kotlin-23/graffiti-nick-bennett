package edu.cnm.deepdive.graffiti.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.graffiti.databinding.FragmentMainBinding;

@AndroidEntryPoint
public class MainFragment extends Fragment {

  private FragmentMainBinding binding;
  // TODO: 7/20/26 Add fields for relevant viewmodels, etc.

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentMainBinding.inflate(inflater, container, false);
    // TODO: 7/20/26 Attach listeners.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // TODO: 7/20/26 Get reference to relevant viewmodel(s); attach observers.
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
