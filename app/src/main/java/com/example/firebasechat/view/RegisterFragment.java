package com.example.firebasechat.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasechat.R;
import com.example.firebasechat.databinding.FragmentLoginBinding;
import com.example.firebasechat.databinding.FragmentRegisterBinding;
import com.example.firebasechat.viewmodel.LoginViewModel;

public class RegisterFragment extends Fragment {

    private LoginViewModel viewModel;
    private FragmentRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n") //para settext //ELIMINAR
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.txtEmailAddress.setText("admin123@gmail.com");  //ELIMINAR
        binding.txtPassword.setText("admin123");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
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
                    createAccount(email, password);
                }

            }
        });

        //Check if Register was successful
        viewModel.isSignupSuccessful().observe(getViewLifecycleOwner(), isSignupSuccessful ->{
            if (isSignupSuccessful){
                Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                requireActivity().finish();
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

        return binding.getRoot();
    }

    public void createAccount(String email, String password) {
        viewModel.createUserWithEmailAndPassword(email, password);
    }
}