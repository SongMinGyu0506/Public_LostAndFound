package com.example.lostandfind.activity.my;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.lostandfind.R;
import com.example.lostandfind.adapter.Main2Adapter;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyPost extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spMySearchPost1, spMySearchPost2;
    Button btnSearchMyPost;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    ArrayList<LostPostInfo> lostPostInfoArrayList = new ArrayList<LostPostInfo>();
    ArrayList<Post> postArrayList = new ArrayList<Post>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        setFindViewByid();
        setMySearchPostSpinner1();
        setMySearchPostSpinner2();

        adapter = new Main2Adapter(lostPostInfoArrayList,MyPost.this);
        recyclerView.setAdapter(adapter);

        setSearchEvent();

    }

    private void setSearchEvent() {
        btnSearchMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerText = spMySearchPost1.getSelectedItem().toString().trim();
                if (spinnerText.equals("분실물")) {
                    lostPostInfoArrayList.clear();
                    postArrayList.clear();
                    adapter.notifyDataSetChanged();
                    adapter = new Main2Adapter(lostPostInfoArrayList,MyPost.this);
                    recyclerView.setAdapter(adapter);

                    searchQuery("분실물");
                } else {
                    lostPostInfoArrayList.clear();
                    postArrayList.clear();
                    adapter.notifyDataSetChanged();
                    adapter = new MainAdapter(MyPost.this,postArrayList);
                    recyclerView.setAdapter(adapter);

                    searchQuery("습득물");
                }
            }
        });
    }
    private void executeAllQuery(String type) {
        if (type.equals("분실물")) {
            db.collection("LostPosts")
                    .whereEqualTo("writerUID",user.getUid())
                    .orderBy("postDate", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    LostPostInfo post = document.toObject(LostPostInfo.class);
                                    post.setId(document.getId());
                                    lostPostInfoArrayList.add(post);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else {
            db.collection("Posts")
                    .whereEqualTo("writerUID",user.getUid())
                    .orderBy("postDate", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Post post = document.toObject(Post.class);
                                    post.setId(document.getId());
                                    postArrayList.add(post);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void executeSearchQuery(String type) {
        if (type.equals("분실물")) {
            db.collection("LostPosts")
                    .whereEqualTo("writerUID",user.getUid())
                    .whereEqualTo("category",spMySearchPost2.getSelectedItem().toString().trim())
                    .orderBy("postDate", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    LostPostInfo post = document.toObject(LostPostInfo.class);
                                    post.setId(document.getId());
                                    lostPostInfoArrayList.add(post);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else {
            db.collection("Posts")
                    .whereEqualTo("writerUID",user.getUid())
                    .whereEqualTo("category",spMySearchPost2.getSelectedItem().toString().trim())
                    .orderBy("postDate",Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Post post = document.toObject(Post.class);
                                    post.setId(document.getId());
                                    postArrayList.add(post);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void searchQuery(String type) {
        if (type.equals("분실물")) {
            if (spMySearchPost2.getSelectedItem().toString().trim().equals("전체")) {
                executeAllQuery(type);
            } else {
                executeSearchQuery(type);
            }
        } else {
            if (spMySearchPost2.getSelectedItem().toString().trim().equals("전체")) {
                executeAllQuery(type);
            } else {
                executeSearchQuery(type);
            }
        }
    }

    private void setMySearchPostSpinner1() {
        ArrayList<String> category = new ArrayList<String>();
        category.add("습득물");
        category.add("분실물");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, category);
        ((Spinner)findViewById(R.id.spMySearchPost1)).setAdapter(adapter);
        spMySearchPost1 = (Spinner) findViewById(R.id.spMySearchPost1);
        spMySearchPost1.setSelection(0);
    }

    private void setMySearchPostSpinner2() {
        ArrayList<String> category = new ArrayList<String>();
        category.add("전체");
        category.add("가방");
        category.add("귀금속");
        category.add("도서");
        category.add("스포츠용품");
        category.add("악기");
        category.add("의류");
        category.add("전자기기");
        category.add("지갑");
        category.add("카드");
        category.add("현금");
        category.add("휴대폰");
        category.add("기타");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, category);
        ((Spinner)findViewById(R.id.spMySearchPost2)).setAdapter(adapter);
        spMySearchPost2 = (Spinner) findViewById(R.id.spMySearchPost2);
        spMySearchPost2.setSelection(0);
    }

    private void setFindViewByid() {
        btnSearchMyPost = (Button) findViewById(R.id.btnSearchMyPost);
        recyclerView = (RecyclerView) findViewById(R.id.MyRecyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}