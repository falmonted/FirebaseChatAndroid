package com.example.firebasechat.view.auth;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasechat.R;
import com.example.firebasechat.databinding.FragmentLoginBinding;
import com.example.firebasechat.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    private LoginViewModel viewModel;

    private FragmentLoginBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @SuppressLint("SetTextI18n") //para settext //ELIMINAR
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.txtEmailAddress.setText("admin123@gmail.com");  //ELIMINAR
        binding.txtPassword.setText("admin123");


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmailAddress.getText().toString().trim();
                String password = binding.txtPassword.getText().toString();

                binding.txtEmailAddress.setError(null);
                binding.txtPassword.setError(null);

                boolean isValid = true;

                if (email.isBlank()) {
                    binding.txtEmailAddress.setError("El correo es obligatorio");
                    isValid = false;
                }
                if (password.isBlank()) {
                    binding.txtPassword.setError("El contraseña  es obligatoria");
                    isValid = false;
                }
                if (isValid) {
                    signIn(email, password);
                }

            }
        });

        //FireAuthRepository errors
        viewModel.authError().observe(getViewLifecycleOwner(), authError -> {
            if (authError == null) {
                return;
            }
            switch (authError) {
                case INVALID_CREDENTIAL:
                    binding.txtEmailAddress.setError("El correo o la contraseña no son válidos.");
                    break;

                case WRONG_PASSWORD:
                    binding.txtPassword.setError("La contraseña es incorrecta.");
                    break;

                case USER_NOT_FOUND:
                    binding.txtEmailAddress.setError("No existe un usuario con este correo electrónico.");
                    break;

                case INVALID_EMAIL:
                    binding.txtEmailAddress.setError("El correo electrónico no tiene un formato válido.");
                    break;

                case EMAIL_ALREADY_IN_USE:
                    binding.txtEmailAddress.setError("Este correo electrónico ya está registrado.");
                    break;

                case WEAK_PASSWORD:
                    binding.txtPassword.setError("La contraseña es demasiado débil.");
                    break;

                case USER_DISABLED:
                    Toast.makeText(getContext(), "Esta cuenta ha sido deshabilitada.", Toast.LENGTH_LONG).show();
                    break;

                case TOO_MANY_REQUESTS:
                    Toast.makeText(getContext(), "Demasiados intentos. Inténtalo nuevamente más tarde.", Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(getContext(), "Ha ocurrido un error de autenticación", Toast.LENGTH_LONG).show();
                    break;
            }

        });


        binding.btnSignup.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(
                    R.id.action_loginFragment_to_registerFragment
            );
        });

        return binding.getRoot();
    }

    public void signIn(String email, String password) {
        viewModel.signInWithEmailAndPassword(email, password);
    }

}