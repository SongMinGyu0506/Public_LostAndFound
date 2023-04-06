package com.example.lostandfind.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.chat.ChatActivity;
import com.example.lostandfind.chatDB.ChatRooms;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder>{
    private final static String TAG = "roomAdapter";

    List<ChatRooms> messages;
    private Context context;

    String curUserUID;

    public ChatRoomAdapter(String curUserUID, Context context) {
        this.curUserUID = curUserUID;
        this.context = context;
        messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChatRoomAdapter.ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_chat_room, parent, false);
        ChatRoomHolder holder = new ChatRoomHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {
        ChatRooms chatRoom = messages.get(position);
        holder.setItem(messages.get(position));
        Log.d(TAG, "chatroomid: "+chatRoom.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatRoomUserData", chatRoom);
                intent.putExtra("exist", true);
                intent.putExtra("roomId", chatRoom.getId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setData(List<ChatRooms> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }

    public class ChatRoomHolder extends RecyclerView.ViewHolder{
        private TextView roomName;
        private TextView roomContents;
        private TextView roomLastTime;

        public ChatRoomHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.roomName);
            roomContents = itemView.findViewById(R.id.roomContents);
            roomLastTime = itemView.findViewById(R.id.roomLastTime);
        }

        public void setItem(ChatRooms item) {
            roomContents.setText(item.getLastChat());
            roomName.setText(item.getReceiverName());
            roomLastTime.setText(item.getLastChatTime());

            Log.d(TAG, "item last chat: "+item.getLastChat());
            Log.d(TAG, "item last chat time: "+item.getLastChatTime());
            Log.d(TAG, "item receiver name: "+item.getReceiverName());
        }
    }
}
