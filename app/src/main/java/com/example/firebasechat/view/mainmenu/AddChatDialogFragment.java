package com.example.firebasechat.view.mainmenu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.firebasechat.viewmodel.ChatsViewModel;
import com.example.firebasechat.viewmodel.LoginViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasechat.databinding.FragmentAddChatDialogBinding;

public class AddChatDialogFragment extends BottomSheetDialogFragment {

    private FragmentAddChatDialogBinding binding;
    private ChatsViewModel chatsViewModel;
    private String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddChatDialogBinding.inflate(inflater, container, false);
        chatsViewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);

        // btnAddChat
        binding.btnAddUserChat.setOnClickListener(v -> {
            email = binding.txtEmail.getText().toString().trim();
            binding.txtEmail.setError(null);
            if (email.isBlank()) {
                binding.txtEmail.setError("Agrega un email");
                binding.txtEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.txtEmail.setError("Introduce un email válido");
                binding.txtEmail.requestFocus();
                return;
            }
            binding.btnAddUserChat.setEnabled(false);
            addChat(email);
        });

        chatsViewModel.isChatAdded().observe(getViewLifecycleOwner(), isChatAdded -> {
           if (isChatAdded) {
               Toast.makeText(getContext(), "Chat Agregado Exitosamente", Toast.LENGTH_SHORT).show();
               dismiss();
           }
        });

        chatsViewModel.getError().observe(getViewLifecycleOwner(), exception -> {
            if (exception != null){
                Toast.makeText(getContext(), "Error al Intentar Agregar Chat", Toast.LENGTH_SHORT).show();
                binding.btnAddUserChat.setEnabled(true);
            }
        });

        return binding.getRoot();
    }

    void addChat(String email){
        chatsViewModel.addChat(email);
    }
}