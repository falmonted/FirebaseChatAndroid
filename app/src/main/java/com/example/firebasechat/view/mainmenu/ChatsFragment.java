package com.example.firebasechat.view.mainmenu;

import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasechat.R;
import com.example.firebasechat.databinding.FragmentChatsBinding;
import com.example.firebasechat.databinding.FragmentMessagesBinding;
import com.example.firebasechat.model.Chat;
import com.example.firebasechat.viewmodel.ChatsViewModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Chat> chatsModels;
    private ChatsViewModel chatsViewModel;

    public ChatsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
//    public static ChatsFragment newInstance(String param1, String param2) {
//
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        chatsModels = new ArrayList<>();
        Chat chat1 = new Chat("123", Timestamp.now(), Chat.ChatType.INDIVIDUAL, List.of("1", "2"));
        Chat chat2 = new Chat("124", Timestamp.now(), Chat.ChatType.INDIVIDUAL, List.of("3", "4"));
        Chat chat3 = new Chat("124", Timestamp.now(), Chat.ChatType.INDIVIDUAL, List.of("3", "4"));
        chat1.setLastMessage("klk");
        chat1.setLastMessageTimestamp(Timestamp.now());
        chat2.setLastMessage("klk");
        chat2.setLastMessageTimestamp(Timestamp.now());
        chat3.setLastMessage("klk");
        chat3.setLastMessageTimestamp(Timestamp.now());
        chatsModels.add(chat1);
        chatsModels.add(chat2);
        chatsModels.add(chat3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatsBinding.inflate(inflater, container, false);

        // RecyclerView
        RecyclerView recyclerView = binding.chatsRecyclerView;
        // Adapter and Layout
        adapter = new ChatRecyclerViewAdapter(getContext(), chatsModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);



        chatsViewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        //AddChat
        binding.btnAddChat.setOnClickListener(v -> {
            chatsViewModel.resetAddChatState();
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_chatsFragment_to_addChatDialogFragment);
        });
        return binding.getRoot();
    }
}