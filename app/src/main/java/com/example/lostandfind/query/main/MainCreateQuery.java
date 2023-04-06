package com.example.lostandfind.query.main;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lostandfind.data.Post;
import com.example.lostandfind.data.UserData;
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
import com.google.firebase.storage.UploadTask;

public class MainCreateQuery {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private Context context;
    private UserData tmp_user;
    public MainCreateQuery(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.storage = FirebaseStorage.getInstance();
        this.context = context;
    }

    public void getUserData() {

        db.collection("Users")
                .whereEqualTo("uid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserData tmp = document.toObject(UserData.class);
                        Log.d(TAG,"Developer Test: "+tmp.getName());
                    }
                }
            }
        });
    }

    public void imageUpload(Uri uri, String imageName) {
        StorageReference storageRef = storage.getReference();
        StorageReference photoRef = storageRef.child("photo/"+imageName);
        UploadTask uploadTask = photoRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Developer Error Log: Image Upload Error",e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerPost(Post temp_post) {
        db.collection("Posts").add(temp_post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"Developer Success Log: Success Register Post");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Developer Error Log: Post Register Error:",e);
                    }
                });
    }

    public String getUserEmail() {
        return user.getEmail().toString();
    }
    public String getUserUid() {
        return user.getUid().toString();
    }
    public String getUserName() {
        getUserData();
        return tmp_user.getName();
    }
}
