package com.example.firebasechat.view.mainmenu;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasechat.R;
import com.example.firebasechat.databinding.ActivityMainMenuBinding;
import com.example.firebasechat.model.Chat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    ArrayList<Chat> chatsModels = new ArrayList<>();
    private ActivityMainMenuBinding binding;
    private ChatRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Creating navController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(binding.navHostMenu.getId());
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = binding.bottomNavigationView;

            NavigationUI.setupWithNavController(bottomNav, navController);
        }

//        // RecyclerView
//        binding.chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Adapter
//        adapter = new ChatRecyclerViewAdapter(this, chatsModels);
//        binding.chatsRecyclerView.setAdapter(adapter);
//
//
//            RecyclerView recyclerView = binding.chatsRecyclerView;
//
//
//            //Bring models before adapter
//            ChatRecyclerViewAdapter adapter = new ChatRecyclerViewAdapter(this, chatsModels);
//            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));



//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

    }
}