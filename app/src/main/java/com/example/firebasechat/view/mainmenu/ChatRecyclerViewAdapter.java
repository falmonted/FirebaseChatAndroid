package com.example.firebasechat.view.mainmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasechat.R;
import com.example.firebasechat.model.Chat;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<Chat> chatsModels;

    public ChatRecyclerViewAdapter (Context context, ArrayList<Chat> chatsModels) {
        this.context = context;
        this.chatsModels = chatsModels;
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Layout inflate for rows and defines which view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ChatRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.MyViewHolder holder, int position) {
        //Assigning values to each row depending on position on screen
        holder.chatContent.setText(chatsModels.get(position).getLastMessage());
        holder.chaTimestamp.setText(chatsModels.get(position).getLastMessageTimestamp().toString());
    }

    @Override
    public int getItemCount() {
        return chatsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //Grabs the views from our recycler_view_row
//        ImageView imageView;
        TextView chatName, chatContent, chaTimestamp;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

//            imageView = itemView.findViewById(R.id.chatImage);
//            chatName = itemView.findViewById(R.id.chatNameView);
            chatContent = itemView.findViewById(R.id.chatContentView);
            chaTimestamp = itemView.findViewById(R.id.chatTimeView);
        }
    }
}
