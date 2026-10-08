package com.example.firebasechat.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasechat.databinding.FragmentMessagesBinding;
import com.example.firebasechat.viewmodel.MessagesViewModel;

public class MessagesFragment extends Fragment {

    private MessagesViewModel mViewModel;
    private FragmentMessagesBinding binding;

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMessagesBinding.inflate(inflater, container, false );

        return binding.getRoot();
    }

}