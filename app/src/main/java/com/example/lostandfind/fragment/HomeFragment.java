package com.example.lostandfind.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainActivity;
import com.example.lostandfind.activity.Main.MainCreateActivity;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ViewGroup rootView;

    RecyclerView recyclerView;
    ArrayList<Post> postArrayList = new ArrayList<Post>();
    MainAdapter mainAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Spinner sp_Search;
    Button btn_Search;
    SwipeRefreshLayout swipeRefreshLayout;

    Spinner spinner;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mainAdapter = new MainAdapter(getActivity(), postArrayList);

        setFindViewById();
        setRecyclerView();
        setSpinnerData(sp_Search);

        CollectionReference collectionReference = db.collection("Posts");
        Query query = collectionReference.orderBy("postDate",Query.Direction.DESCENDING);

        //Swipe Action
        loadSwiper(swipeRefreshLayout,query,collectionReference);

        //FloatButton
        fabsetOnClickListener();

        //SearchEvent
        searchClickEvent(btn_Search,query);

        return rootView;
    }

    private void fabsetOnClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mainAdapter);
    }

    private void setFindViewById() {
        swipeRefreshLayout = rootView.findViewById(R.id.layout_swipe);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        btn_Search = (Button)rootView.findViewById(R.id.btn_Search);
        spinner = (Spinner)rootView.findViewById(R.id.sp_Search);
        // FloatingActionButton
        fab = (FloatingActionButton) rootView.findViewById(R.id.temp_upperBtn);
    }


    @Override
    public void onResume() {
        super.onResume();
        CollectionReference collectionReference = db.collection("Posts");
        Query query = collectionReference.orderBy("postDate",Query.Direction.DESCENDING);
        postArrayList.clear();
        mainAdapter.clear();
        excuteQuery(query,collectionReference);
        sp_Search.setSelection(0);
    }

    private void searchClickEvent(Button btn_search, Query query) {

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postArrayList.clear();
                mainAdapter.clear();
                searchPosts(query);

            }
        });
    }

    private void searchPosts(Query query){
        String toString2 = sp_Search.getSelectedItem().toString().trim();
        db = FirebaseFirestore.getInstance();
        if (toString2 == "전체") {
            db.collection("Posts")
                    .orderBy("postDate",Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Post post = document.toObject(Post.class);
                                    post.setId(document.getId());
                                    postArrayList.add(post);
                                }
                            } else {
                                Log.e(TAG,"Developer Log:", task.getException());
                            }
                            mainAdapter.notifyDataSetChanged();
                            //lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);
                        }
                    });
        }else {
            db.collection("Posts")
                    .whereEqualTo("category",toString2)
                    .orderBy("postDate", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Post post = document.toObject(Post.class);
                                    post.setId(document.getId());
                                    postArrayList.add(post);
                                }
                            } else {
                                Log.e(TAG,"Developer Log:", task.getException());
                            }
                            mainAdapter.notifyDataSetChanged();
//                            if (task.getResult().size() != 0)
//                                lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"Developer Log: NULL!",e);
                }
            });
        }
    }

    private void setSpinnerData(Spinner sp_search) {
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
        ((Spinner)rootView.findViewById(R.id.sp_Search)).setAdapter(adapter);
        sp_Search = (Spinner) rootView.findViewById(R.id.sp_Search);
        sp_Search.setSelection(0);

    }


//    private void nextQueryExcute(Query nextQuery) {
//        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    for (DocumentSnapshot d: task.getResult()) {
//                        Post post = d.toObject(Post.class);
//                        post.setId(d.getId());
//                        postArrayList.add(post);
//                    }
//                    mainAdapter.notifyDataSetChanged();
//                    if (task.getResult().size() != 0) {
//                        lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);
//                    }
//                    if (task.getResult().size() <limit) {
//                        isLastItemReached = true;
//                    }
//                }
//            }
//        });
//    }

    private void loadSwiper(SwipeRefreshLayout swipeRefreshLayout, Query query, CollectionReference collectionReference) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getActivity(), "testSwipe", Toast.LENGTH_SHORT).show();
                postArrayList.clear();
                mainAdapter.clear();
                excuteQuery(query,collectionReference);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void excuteQuery(Query query, CollectionReference collectionReference) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        postArrayList.add(post);
                    }
                    mainAdapter.notifyDataSetChanged();
                    //lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);
                }
            }
        });
    }
}
//        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
//                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//                int visibleItemCount = linearLayoutManager.getChildCount();
//                int totalItemCount = linearLayoutManager.getItemCount();
//
//                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
//                    isScrolling = false;
//                    Query nextQuery = collectionReference.orderBy("title", Query.Direction.ASCENDING).startAfter(lastVisible).limit(limit);
//                    nextQueryExcute(nextQuery);
//                }
//            }
//        };
//        recyclerView.addOnScrollListener(onScrollListener);