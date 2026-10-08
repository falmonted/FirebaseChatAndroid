package com.example.firebasechat.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.firebasechat.R;
import com.example.firebasechat.view.mainmenu.MainMenuActivity;
import com.example.firebasechat.viewmodel.LoginViewModel;

public class AuthActivity extends AppCompatActivity {
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Checks if user has already log
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

       viewModel.userSignOut();

        if (viewModel.isUserLog()) {
            Intent intent = new Intent(AuthActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        viewModel.isLoginSuccessful().observe(this, isLoginSuccessful -> {
            Intent intent = new Intent(AuthActivity.this, MainMenuActivity.class);
            if (Boolean.TRUE.equals(isLoginSuccessful)){
                Toast.makeText(this, "Inicio de Sesión Exitoso", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });


    }
}