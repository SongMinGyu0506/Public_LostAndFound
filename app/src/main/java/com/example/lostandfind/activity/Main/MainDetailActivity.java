package com.example.lostandfind.activity.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main2.Main2UpdateActivity;
import com.example.lostandfind.activity.chat.ChatActivity;
import com.example.lostandfind.activity.chatting.MainChattingActivity;
import com.example.lostandfind.chatDB.ChatRooms;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.data.UserData;
import com.example.lostandfind.query.main.MainInspectQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainDetailActivity extends AppCompatActivity {
    private final static String TAG = "MainDetailActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Post post;
    String myName, myUID;
    ChatRooms chatRoom;
    Boolean exist = false;  //default
    String oldRoomId;
    String newRoomId = db.collection("LostPosts").document().getId();

    TextView title, contents, location, lostDate, postDate, name, category;
    ImageView image;
    Toolbar toolbar;

    TextView update_btn, delete_btn;
    Button chat_btn;
    TextView notice_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        initializeView();               // view 초기화
        setActionbar();                 // Actionbar 관련 설정
        getIntentData();                // 넘어오는 Intent data get
        setStorageImage(post, image);   // image get, imageView set
        setTextView();                  //TextView set

        getUserData();
        getRoomId();
        update_btn.setOnClickListener(onClickListener);
        delete_btn.setOnClickListener(onClickListener);
        chat_btn.setOnClickListener(onClickListener);

//        String temp_email = user.getEmail();
//        String postEmail = post.getWriterEmail();
//        try {
//            if (!postEmail.equals(temp_email)) {
//                update_btn.setVisibility(View.INVISIBLE);
//                delete_btn.setVisibility(View.INVISIBLE);
//            }
//        } catch (Exception e) {
//            update_btn.setEnabled(false);
//            delete_btn.setEnabled(false);
//            Log.e(TAG,"Developer Error Log: ",e);
//        }

        setBtnVisibility();
    }

    //버튼 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.update_btn:
                    updatePost();
                    break;
                case R.id.delete_btn:
                    deletePostAlert();
                    break;
                case R.id.chat_btn:
                    enterChat();
                    break;
            }
        }
    };

    private void setBtnVisibility(){
        String writerUID = post.getWriterUID();
        if (writerUID.equals(user.getUid()) == false){
            update_btn.setVisibility(View.INVISIBLE);
            delete_btn.setVisibility(View.INVISIBLE);
            notice_text.setVisibility(View.INVISIBLE);
        }
        if (writerUID.equals(user.getUid()) == true){
            chat_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void getRoomId(){
        if (post.getWriterUID().equals(user.getUid()) == false) {// 본인이 작성한 글은 채팅걸기를 막음
            Log.d(TAG, "getRoomId method");
            Log.d(TAG, "getWriterUID: "+post.getWriterUID());
            Log.d(TAG, "getUserUid: "+user.getUid());
            Log.d(TAG, "newRoomId: "+newRoomId);
            if (post.getWriterUID().equals(user.getUid()) == false) {    //본인이 작성한 글은 채팅걸기를 막음
                db.collection("ChatRoom").document(user.getUid())
                        .collection("userRoom")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        ChatRooms chatRooms = document.toObject(ChatRooms.class); //여기
                                        Log.d(TAG, "chatrooms get id: "+chatRooms.getId());
                                        if ((chatRooms.getReceiverUID()).equals(post.getWriterUID())) {
                                            exist = true;
                                            chatRoom = chatRooms;
                                            oldRoomId = chatRoom.getId();
                                            Log.d(TAG, "old room id: "+oldRoomId);
                                            Log.d(TAG, "new room id: "+newRoomId);
                                            Log.d(TAG, "exist: "+exist);
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        }
    }

    public void getUserData() {
        DocumentReference dRef = db.collection("Users").document(user.getUid());
        dRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            myName = (documentSnapshot.toObject(UserData.class).getName());
                            myUID = (documentSnapshot.toObject(UserData.class).getUID());
                            Log.d(TAG, "get sender name: "+myName);
                            Log.d(TAG, "get sender id: "+myUID);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Developer: Error: ",e);
                    }
                });
    }

    private void enterChat(){
        if (post.getWriterUID().equals(myUID) == false){    // 본인이 작성한 글은 채팅걸기를 막음
            Intent intent = new Intent(this, ChatActivity.class);

            if (exist){
                ChatRooms chatRoomUserData = new ChatRooms(oldRoomId, post.getWriterUID(), post.getName(),
                        myUID, myName);
                intent.putExtra("chatRoomUserData", chatRoomUserData);
                intent.putExtra("exist", exist);
                intent.putExtra("roomId", oldRoomId);
            } else {
                ChatRooms chatRoomUserData = new ChatRooms(newRoomId, post.getWriterUID(), post.getName(),
                        myUID, myName);
                intent.putExtra("chatRoomUserData", chatRoomUserData);
                intent.putExtra("exist", exist);
                intent.putExtra("roomId", newRoomId);
            }
            startActivity(intent);
        }
    }

    private void deletePost(){
        db.collection("Posts").document(post.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast("게시글 삭제 완료");
                            finish();
                        }
                    }
                });
    }

    private void deletePostAlert() {
//        Toast.makeText(getApplicationContext(), "되나?", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제 알림");
        builder.setMessage("게시글을 정말로 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePost();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void updatePost() {
        finish();
        Intent intent = new Intent(this, MainUpdateActivity.class);
        intent.putExtra("upDatePost", post);
        startActivity(intent);
    }

    public void initializeView()
    {
        toolbar = findViewById(R.id.toolbar);
        title = (TextView)findViewById(R.id.title);
        contents = (TextView)findViewById(R.id.contents);
        location = (TextView)findViewById(R.id.location);
        lostDate = (TextView)findViewById(R.id.lostDate);
        postDate = (TextView)findViewById(R.id.postDate);
        name = (TextView)findViewById(R.id.name);
        category = (TextView)findViewById(R.id.category);
        image = (ImageView)findViewById(R.id.image);

        update_btn = (TextView)findViewById(R.id.update_btn);
        delete_btn = (TextView)findViewById(R.id.delete_btn);
        chat_btn = (Button)findViewById(R.id.chat_btn);
        notice_text = (TextView) findViewById(R.id.notice_text);
    }

    private void setActionbar(){
        setSupportActionBar(toolbar);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }

    private void setTextView(){
        title.setText(post.getTitle());
        contents.setText(post.getContents());
        location.setText(post.getLocation());
        lostDate.setText(post.getLostDate());
        postDate.setText(post.getPostDate());
        name.setText(post.getName());
        category.setText(post.getCategory());
        //toast("uid: "+post.getWriterUID());
    }

    private void getIntentData(){
        Intent intent = getIntent();
        post = (Post)intent.getSerializableExtra("post");
    }


    public void setStorageImage(Post post, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("photo/"+post.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image.setImageResource(R.drawable.kumoh_symbol);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //토스트 메소드 간략화
    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}