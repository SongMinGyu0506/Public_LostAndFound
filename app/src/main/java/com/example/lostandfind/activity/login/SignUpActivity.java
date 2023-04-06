package com.example.lostandfind.activity.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private Button cancel,register;
    private EditText mName,email,pw,pw_c;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //test!!
        cancel = (Button)findViewById(R.id.btn_Cancel);
        register = (Button)findViewById(R.id.btn_Register);
        mName = (EditText)findViewById(R.id.inputText_Name);
        email = (EditText)findViewById(R.id.inputText_Email);
        pw = (EditText)findViewById(R.id.inputText_PW);
        pw_c = (EditText)findViewById(R.id.inputText_PWCorrect);

        firebaseAuth = FirebaseAuth.getInstance();

//        TODO: [SignUpActivity] 아래의 ActionBar 내용 확인 후 지우기
        // !!!!! ActionBar는 values-theme에서 수정하였음 !!!!!
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mEmail = email.getText().toString().trim();
                String mPw = pw.getText().toString().trim();
                String mPw_c = pw_c.getText().toString().trim();
                if (mEmail.contains("@kumoh.ac.kr")) {
                    if (mPw.equals(mPw_c)) {
                        Log.d(TAG,"등록 버튼"+mEmail+" , "+pw);
                        final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                        mDialog.setMessage("가입중입니다...");
                        mDialog.show();

                        firebaseAuth.createUserWithEmailAndPassword(mEmail,mPw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mDialog.dismiss();

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String email = user.getEmail();
                                    String uid = user.getUid();
                                    String name = mName.getText().toString().trim();

//                                HashMap<Object,String> hashMap = new HashMap<>();

//                                hashMap.put("uid",uid);
//                                hashMap.put("email",email);
//                                hashMap.put("name",name);

                                    UserData userData = new UserData(uid,email,name);
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference reference = db.collection("Users").document(userData.getUID());
                                    reference.set(userData);

                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG,"Sent Email");
                                                Toast.makeText(SignUpActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.e(TAG, "sendEmailVerification", task.getException());
                                                Toast.makeText(SignUpActivity.this,
                                                        "Failed to send verification email.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(SignUpActivity.this,"회원가입에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this,"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this,"비밀번호가 일치하지 않습니다. 다시 입력해주세요.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this);
                    dlg.setTitle("다른 이메일 사용");
                    dlg.setMessage("금오공대 웹 메일로 등록해주세요");
                    dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            email.setText("");
                        }
                    });
                    dlg.show();
                }
            }
        });
    }
}