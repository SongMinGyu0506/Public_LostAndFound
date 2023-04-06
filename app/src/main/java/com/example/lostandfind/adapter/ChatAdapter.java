package com.example.lostandfind.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfind.R;

import com.example.lostandfind.chatDB.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{
    private final static String TAG = "ChatAdapter";
    static int MY_MSG = 1;
    static int OTHER_MSG = 2;

    String curUserUID;

    List<Chat> messages;

    public ChatAdapter(String curUserUID) {
        this.curUserUID = curUserUID;
        messages = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSenderUID().equals(curUserUID)){
            Log.d(TAG, "equal true");
            return MY_MSG;
        }else{
            Log.d(TAG, "equal false");
            return OTHER_MSG;
        }
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatBubble;

        Log.d(TAG, "viewType: "+viewType);

        if (viewType == MY_MSG){
            chatBubble = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_container_send_message, parent, false);
            return new ChatHolder(chatBubble);
        } else {
            chatBubble = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_container_receive_message, parent, false);
            return new ChatHolder(chatBubble);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        holder.setItem(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setData(List<Chat> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        private TextView textMessage;
        private TextView textDateTime;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.textMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
        }

        public void setItem(Chat item) {
            textMessage.setText(item.getText());
            textDateTime.setText(item.getSendDate());
        }
    }
}
