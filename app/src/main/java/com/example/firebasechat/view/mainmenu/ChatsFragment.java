package com.example.firebasechat.view.mainmenu;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
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
import com.example.firebasechat.repository.user.FireStoreUserRepository;
import com.example.firebasechat.viewmodel.ChatsViewModel;
import com.example.firebasechat.viewmodel.UserViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.messaging.FirebaseMessaging;

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

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d(TAG, "Notification permission granted");
                    fetchAndSaveFcmToken();
                } else {
                    Log.w(TAG, "Notification permission denied");
                }
            });

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

        // Ask for notification permission
        askNotificationPermission();

        return binding.getRoot();
    }

    private void fetchAndSaveFcmToken() {
        if (userViewModel != null) {
            String currentUserId = userViewModel.getCurrentLogUserId();
            if (currentUserId != null) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                String token = task.getResult();
                                FireStoreUserRepository.getInstance().setFcmToken(currentUserId, token);
                            }
                        });
            }
        }
    }

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                fetchAndSaveFcmToken();
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            fetchAndSaveFcmToken();
        }
    }
}