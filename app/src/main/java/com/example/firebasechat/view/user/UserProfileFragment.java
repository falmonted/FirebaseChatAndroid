package com.example.firebasechat.view.user;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasechat.R;
import com.example.firebasechat.model.User;
import com.example.firebasechat.viewmodel.UserViewModel;
import com.example.firebasechat.databinding.FragmentUserProfileBinding;

public class UserProfileFragment extends Fragment {

    private UserViewModel viewModel;
    private FragmentUserProfileBinding binding;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);

        // Updated User Profile
        viewModel.getCurrentUserFirestore();
        viewModel.getCurrentLogUser().observe(getViewLifecycleOwner(), logUser -> {
            if (logUser != null) {
                setUserProfileData(logUser);
            }
        });

        binding.btnEdit.setOnClickListener(v -> {
            binding.txtName.setEnabled(true);
        });

        binding.btnUpdateProfile.setOnClickListener(v -> {
            binding.txtName.setError(null);
            boolean isValid = true;

            String name = binding.txtName.getText().toString().trim();
            if (name.isBlank()){
                binding.txtName.setError("El nombre es obligatorio");
                isValid = false;
            }
            if (isValid){
                updateProfile(name);
                binding.txtName.setEnabled(false);
            }
        });

        viewModel.isUserUpdateSuccessful().observe(getViewLifecycleOwner(), isUserUpdated -> {
            if (isUserUpdated) {
                Toast.makeText(requireContext(), "Perfil Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.userModifiedException().observe(getViewLifecycleOwner(), exception -> {
            if (exception != null) {
                Toast.makeText(requireContext(), "Error al Actualizar Pefil", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    public void updateProfile (String name) {
        User user =  new User();
        user.setUsername(name);
        viewModel.updateUserProfile(user);
    }

    public void setUserProfileData (User user) {
        binding.txtName.setText(user.getUsername());
        binding.txtEmailAddress.setText(user.getEmail());
        //TODO: add image
    }

}