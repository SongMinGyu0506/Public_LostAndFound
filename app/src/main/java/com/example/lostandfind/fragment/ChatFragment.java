package com.example.lostandfind.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lostandfind.R;
import com.example.lostandfind.adapter.ChatAdapter;
import com.example.lostandfind.adapter.ChatRoomAdapter;
import com.example.lostandfind.chatDB.Chat;
import com.example.lostandfind.chatDB.ChatRooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class ChatFragment extends Fragment {
    private final static String TAG = "ChatFragment";
    ViewGroup rootView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView recyclerView;
    ChatRoomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchRoom();

        return rootView;
    }

    private void fetchRoom(){
        Log.d(TAG, "fetching rooms");
        adapter = new ChatRoomAdapter(user.getUid(), getActivity());
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "fetch get rooms");
        db.collection("ChatRoom")
                .document(user.getUid())
                .collection("userRoom")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e(TAG, error.getMessage(), error);
                        } else {
                            List<ChatRooms> chatRooms = value.toObjects(ChatRooms.class);
                            adapter.setData(chatRooms);
                            Log.d(TAG, "chatRooms: "+chatRooms);
                        }
                    }
                });
    }
}