package com.example.lostandfind.query.main;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.data.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainAdapterQuery {
    private FirebaseStorage firebaseStorage;
    private Context context;

    public MainAdapterQuery(Context context) {
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.context = context;
    }

    public void getStorageImage(ImageView ivImage, Post post) {
        //파이어베이스에 있는 이미지 저장소의 경로를 가져오고, 이미지를 가져오는 작업 실시
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        //photo가 이미지 저장 디렉토리, post.getImageName()이 가져와야할 이미지 이름
        ref.child("photo/"+post.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //이미지 가져오는데 성공했으면 holder.ivImage에 이미지 삽입
                        Glide.with(context).load(uri).into(ivImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //실패했을 경우 drawable에 있는 kumoh_symbol을 사용
                ivImage.setImageResource(R.drawable.kumoh_symbol);
            }
        });
    }

}
