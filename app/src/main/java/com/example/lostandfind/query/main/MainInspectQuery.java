package com.example.lostandfind.query.main;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainInspectQuery {
    private FirebaseStorage firebaseStorage;
    private Context context;

    public MainInspectQuery(Context context) {
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.context = context;
    }

    public void getStorageImage(Post post, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("photo/"+post.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image.setImageResource(R.drawable.kumoh_symbol);
            }
        });
    }
    public void getStorageImage2(LostPostInfo post, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("photo/"+post.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image.setImageResource(R.drawable.kumoh_symbol);
            }
        });
    }
}
