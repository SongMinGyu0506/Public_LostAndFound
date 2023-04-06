package com.example.lostandfind.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main2.LostPostActivity;
import com.example.lostandfind.adapter.Main2Adapter;
import com.example.lostandfind.data.LostPostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Home2Fragment extends Fragment {
    private final static String TAG = "Home2Fragment";
    ViewGroup rootView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FirebaseFirestore db;
    ArrayList<LostPostInfo> arrayList;
    ProgressDialog progressDialog;

    SwipeRefreshLayout swipeRefreshLayout;

    Button btnSearch2;
    Spinner spSearch2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home2, container, false);

        swipeRefreshLayout = rootView.findViewById(R.id.layout_swipe2);
        btnSearch2 = (Button) rootView.findViewById(R.id.btn_Search2);

        setSpinnerData(spSearch2);

//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();

        db = FirebaseFirestore.getInstance();

        rootView.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayList = new ArrayList<LostPostInfo>();

        adapter = new Main2Adapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);

        loadSwiper(swipeRefreshLayout);

        setSearchEvent(btnSearch2);
        startView();

//        EventChangeListener();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList.clear();
        adapter.notifyDataSetChanged();
        startView();
        spSearch2.setSelection(0);
    }

    private void loadSwiper(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                adapter.notifyDataSetChanged();
                db.collection("LostPosts")
                        .orderBy("postDate", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        LostPostInfo lostPostInfo = document.toObject(LostPostInfo.class);
                                        lostPostInfo.setId(document.getId());
                                        arrayList.add(lostPostInfo);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                swipeRefreshLayout.setRefreshing(false);
                spSearch2.setSelection(0);
            }
        });

    }

    private void setSearchEvent(Button btnSearch2) {
        btnSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                adapter.notifyDataSetChanged();
                searchPosts();

            }
        });
    }

    private void searchPosts() {
        String spinnerText = spSearch2.getSelectedItem().toString().trim();
        db = FirebaseFirestore.getInstance();
        if (spinnerText == "전체") {
            db.collection("LostPosts")
                    .orderBy("postDate",Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    LostPostInfo post = document.toObject(LostPostInfo.class);
                                    post.setId(document.getId());
                                    arrayList.add(post);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

        } else {
            db = FirebaseFirestore.getInstance();
            db.collection("LostPosts")
                    .orderBy("postDate",Query.Direction.DESCENDING)
                    .whereEqualTo("category",spinnerText)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    LostPostInfo post = document.toObject(LostPostInfo.class);
                                    post.setId(document.getId());
                                    Log.d(TAG,"Developer Log:"+post.toString());
                                    arrayList.add(post);
                                }
                                adapter.notifyDataSetChanged();
                            }

                        }
                    });

        }
    }

    private void setSpinnerData(Spinner spSearch22) {
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, category);
        ((Spinner)rootView.findViewById(R.id.sp_Search2)).setAdapter(adapter);
        spSearch2 = (Spinner) rootView.findViewById(R.id.sp_Search2);
        spSearch2.setSelection(0);
    }

    private void startView(){
        db.collection("LostPosts")
                .orderBy("postDate",Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LostPostInfo lostPostInfo = document.toObject(LostPostInfo.class); //여기
                                lostPostInfo.setId(document.getId());
                                arrayList.add(lostPostInfo);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    //오류 두 가지 있어서 폐기 오류 부분은 '여기' 라고 표시함
    private void EventChangeListener(){
        db.collection("LostPosts").orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //여기
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                arrayList.add(dc.getDocument().toObject(LostPostInfo.class)); //여기
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.floatingActionButton:
                Intent intent = new Intent(getActivity(), LostPostActivity.class);
                startActivity(intent);
                break;
        }
    };

    public void toast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}