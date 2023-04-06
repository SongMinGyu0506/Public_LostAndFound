package com.example.lostandfind.query.login;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lostandfind.activity.Main.MainActivity;
import com.example.lostandfind.activity.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAuthQuery {
    private Context context;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public LoginAuthQuery(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("ko");
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setSignOut() {
        auth.signOut();
    }

    public void loginEmailAndPassword(String email, String pw, EditText inputID, EditText inputPW) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,pw)
            .addOnCompleteListener((LoginActivity)context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (user.isEmailVerified()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((LoginActivity) context).finish();
                        } else {
                            Toast.makeText((LoginActivity)context,"Not Vertiy Email",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (user == null) {
                            Toast.makeText((LoginActivity)context,"사용자 계정 없음",Toast.LENGTH_SHORT).show();
                            inputID.setText("");
                            inputPW.setText("");
                            return;
                        }
                        else if (user != null) {
                            Toast.makeText(context, "비밀번호 오류", Toast.LENGTH_SHORT).show();
                            inputPW.setText("");
                            auth.signOut();
                            return ;
                        } else {
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show();
                            inputID.setText("");
                            inputPW.setText("");
                            return;
                        }
                    }
                }
            });
    }
}
