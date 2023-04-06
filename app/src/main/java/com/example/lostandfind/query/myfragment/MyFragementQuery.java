package com.example.lostandfind.query.myfragment;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.lostandfind.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class MyFragementQuery {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;

    public MyFragementQuery() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
    }

    public void setMyFragmentUserData(TextView tvName, TextView tvEmail) {
        DocumentReference dRef = db.collection("Users").document(user.getUid());
        dRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            tvName.setText(documentSnapshot.toObject(UserData.class).getName());
                            tvEmail.setText(documentSnapshot.toObject(UserData.class).getEmail());
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

}
