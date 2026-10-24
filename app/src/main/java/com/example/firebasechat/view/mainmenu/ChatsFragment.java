package com.example.firebasechat.view.mainmenu;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasechat.R;
import com.example.firebasechat.databinding.FragmentChatsBinding;
import com.example.firebasechat.model.Chat;
import com.example.firebasechat.model.User;
import com.example.firebasechat.viewmodel.ChatsViewModel;
import com.example.firebasechat.viewmodel.UserViewModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Chat> chatsModels = new ArrayList<>();
    private ChatsViewModel chatsViewModel;
    private UserViewModel userViewModel;
    private final String TAG = "ChatsFragment";

    public ChatsFragment() {
        // Required empty public constructor
    }

    public interface getSelectChatCallback {
        void onSuccess(String chatId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatsBinding.inflate(inflater, container, false);

        // RecyclerView
        RecyclerView recyclerView = binding.chatsRecyclerView;
        // Adapter and Layout & listener for row/chat selected
        adapter = new ChatRecyclerViewAdapter(getContext(), chatsModels, new ChatRecyclerViewAdapter.OnChatClickListener() {
            //OpenChat
            @Override
            public void onChatClick(String chatSelectedId) {
                ChatsFragmentDirections.ActionChatsToConversation action =
                        ChatsFragmentDirections.actionChatsToConversation();
                action.setChatId(chatSelectedId);
                Navigation.findNavController(requireView()).navigate(action);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        //Initialize viewModels
        chatsViewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        chatsViewModel.listenToUserChats();
        chatsViewModel.getChats().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null) {
                adapter.chatsModels = new ArrayList<Chat>(chats);
                adapter.notifyDataSetChanged();
            }
        });

        //AddChat
        binding.btnAddChat.setOnClickListener(v -> {
            chatsViewModel.resetAddChatState();
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_chatsFragment_to_addChatDialogFragment);
        });
        return binding.getRoot();
    }
}