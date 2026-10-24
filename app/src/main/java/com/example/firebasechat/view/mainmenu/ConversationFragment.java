package com.example.firebasechat.view.mainmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasechat.R;
import com.example.firebasechat.databinding.FragmentConversationBinding;
import com.example.firebasechat.model.Message;
import com.example.firebasechat.viewmodel.ChatsViewModel;
import com.example.firebasechat.viewmodel.MessagesViewModel;
import com.example.firebasechat.viewmodel.UserViewModel;

import java.util.ArrayList;

public class ConversationFragment extends Fragment {

    private ChatsViewModel chatsViewModel;
    private UserViewModel userViewModel;
    private MessagesViewModel messagesViewModel;
    private FragmentConversationBinding binding;
    private String chatId = null;
    private String contactUserId = null;
    private String currentUserId = null;
    private final ArrayList<Message> messagesModels = new ArrayList<>();
    private MessagesRecyclerViewAdapter adapter;

    public ConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConversationBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            ConversationFragmentArgs args = ConversationFragmentArgs.fromBundle(getArguments());
            chatId = args.getChatId();
        }

        // Initialize viewModels
        chatsViewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        messagesViewModel = new ViewModelProvider(requireActivity()).get(MessagesViewModel.class);

        // RecyclerView setup
        RecyclerView recyclerView = binding.recyclerViewMessages;
        currentUserId = userViewModel.getCurrentLogUserId();
        adapter = new MessagesRecyclerViewAdapter(requireContext(), messagesModels, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Chat & Messages initialization
        if (chatId != null) {
            chatsViewModel.getChatById(chatId);
            messagesViewModel.listenToMessages(chatId);
        }

        // Chat observer
        chatsViewModel.getChat().observe(getViewLifecycleOwner(), chat -> {
            if (chat != null) {
                contactUserId = chatsViewModel.getOtherParticipantId(chat.getParticipantIds());
                if (contactUserId != null) {
                    // Start listening to the contact when contactUserId is available
                    userViewModel.listenToUser(contactUserId);
                }
            }
        });

        // User observer
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.lbContactName.setText(user.getUsername());
                // TODO: Add image
            }
        });

        // Messages observer
        messagesViewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            if (messages != null) {
                adapter.setMessages(new ArrayList<>(messages));
                if (!messages.isEmpty()) {
                    binding.recyclerViewMessages.smoothScrollToPosition(messages.size() - 1);
                }
            }
        });

        // Send message button listener
        binding.btnSendMsg.setOnClickListener(v -> {
            String messageText = binding.txtMessagebox.getText().toString().trim();
            if (!messageText.isEmpty() && chatId != null && currentUserId != null) {
                Message message = new Message();
                message.setChatId(chatId);
                message.setSenderId(currentUserId);
                message.setContent(messageText);
                message.setMediaType(Message.MediaType.TEXT);

                messagesViewModel.sendMessage(message);
                binding.txtMessagebox.setText("");
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_conversationFragment_to_chatsFragment);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (messagesViewModel != null) {
            messagesViewModel.stopListeningToMessages();
        }
        if (userViewModel != null) {
            userViewModel.stopListeningUser();
        }
        binding = null;
    }
}
