package com.example.lostandfind.activity.Main;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.fragment.ChatFragment;
import com.example.lostandfind.fragment.Home2Fragment;
import com.example.lostandfind.fragment.HomeFragment;
import com.example.lostandfind.fragment.MyFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    Home2Fragment home2Fragment;
    ChatFragment chatFragment;
    MyFragment myFragment;

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    TextView toolbarTitle;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pushTokenToDB();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bottomNavigationView = findViewById(R.id.nav_bottom);
        toolbarTitle = findViewById(R.id.toolbarTitle);

        // 첫 화면 설정
        toolbarTitle.setText("습득물");
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, homeFragment).commit();  // 출력

        // 바텀 네비게이션 클릭 시 발생하는 메소드
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:     // 첫 번째 아이템 클릭 시, home1 프래그먼트로 이동
                        homeFragment = new HomeFragment();
                        toolbarTitle.setText("습득물");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;

                    case R.id.tab2:     // 두 번째 아이템 클릭 시, home2 프래그먼트로 이동
                        home2Fragment = new Home2Fragment();
                        toolbarTitle.setText("분실물");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, home2Fragment).commit();
                        return true;

                    case R.id.tab3:     // 세 번째 아이템 클릭 시, chat 프래그먼트로 이동
                        chatFragment = new ChatFragment();
                        toolbarTitle.setText("채팅");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
                        return true;

                    case R.id.tab4:     // 네 번째 아이템 클릭 시, my 프래그먼트로 이동
                        myFragment = new MyFragment();
                        toolbarTitle.setText("마이페이지");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void pushTokenToDB() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    Map<String,Object> tempMap = new HashMap<>();
                    tempMap.put("uid",user.getUid());
                    tempMap.put("token",task.getResult());
                    db.collection("Token")
                            .document(user.getUid())
                            .set(tempMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG,"Developer Log: Success pushTokenToDB");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG,"Developer Log: Failed to pushTokenToDB \nError Log:\n",e);
                                }
                            });
                }
            }
        });
    }
}