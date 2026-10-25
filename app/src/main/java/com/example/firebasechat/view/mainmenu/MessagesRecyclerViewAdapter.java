package com.example.firebasechat.view.mainmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasechat.R;
import com.example.firebasechat.model.Message;

import java.util.ArrayList;
import java.util.Objects;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<Message> messagesModels;
    private final String currentUserId;

    public enum MessageType {
        TEXT_SEND,
        TEXT_RECEIVE,
        IMG_SEND,
        IMG_RECEIVE
    }

    public MessagesRecyclerViewAdapter(Context context, ArrayList<Message> messagesModels, String currentUserId) {
        this.context = context;
        this.messagesModels = messagesModels;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messagesModels.get(position);
        boolean isCurrentUser = message.getSenderId() != null && Objects.equals(message.getSenderId(), currentUserId);

        if (message.getMediaType() == Message.MediaType.IMAGE) {
            return isCurrentUser ? MessageType.IMG_SEND.ordinal() : MessageType.IMG_RECEIVE.ordinal();
        } else {
            return isCurrentUser ? MessageType.TEXT_SEND.ordinal() : MessageType.TEXT_RECEIVE.ordinal();
        }
    }

    @NonNull
    @Override
    public MessagesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if (viewType == MessageType.TEXT_SEND.ordinal()) {
            view = inflater.inflate(R.layout.message_text_send, parent, false);
        } else if (viewType == MessageType.TEXT_RECEIVE.ordinal()) {
            view = inflater.inflate(R.layout.message_text_receive, parent, false);
        } else if (viewType == MessageType.IMG_SEND.ordinal()) {
            view = inflater.inflate(R.layout.message_img_send, parent, false);
        } else if (viewType == MessageType.IMG_RECEIVE.ordinal()) {
            view = inflater.inflate(R.layout.message_img_receive, parent, false);
        } else {
            view = inflater.inflate(R.layout.message_text_send, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesRecyclerViewAdapter.MyViewHolder holder, int position) {
        Message message = messagesModels.get(position);
        int viewType = getItemViewType(position);

        if (viewType == MessageType.IMG_SEND.ordinal() || viewType == MessageType.IMG_RECEIVE.ordinal()) {
            if (holder.messageImage != null) {
                holder.messageImage.setVisibility(View.VISIBLE);
                if (message.getMediaUrl() != null && !message.getMediaUrl().isEmpty()) {
                    Glide.with(context)
                            .load(message.getMediaUrl())
                            .placeholder(R.drawable.add_image_vector)
                            .error(R.drawable.add_image_vector)
                            .into(holder.messageImage);
                } else {
                    holder.messageImage.setImageResource(R.drawable.add_image_vector);
                }
            }
        } else {
            if (holder.messageContent != null) {
                holder.messageContent.setText(message.getContent() != null ? message.getContent() : "");
            }
        }
    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messagesModels = messages;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageContent;
        ImageView messageImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageContent = itemView.findViewById(R.id.txtMsgSend);
            if (messageContent == null) {
                messageContent = itemView.findViewById(R.id.txtMsgRecieve);
            }
            messageImage = itemView.findViewById(R.id.imgViewSend);
            if (messageImage == null) {
                messageImage = itemView.findViewById(R.id.imgViewReceive);
            }
        }
    }
}
