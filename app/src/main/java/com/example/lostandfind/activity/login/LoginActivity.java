package com.example.lostandfind.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainActivity;
import com.example.lostandfind.query.login.LoginAuthQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText inputID, inputPW;
    private Button logIn, signUp, findPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private LoginAuthQuery loginAuthQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logIn = (Button)findViewById(R.id.btn_LogIn);
        signUp = (Button)findViewById(R.id.btn_SignUp);
        inputID = (EditText)findViewById(R.id.inputText_ID);
        inputPW = (EditText)findViewById(R.id.inputText_PW);
        findPassword = (Button)findViewById(R.id.btn_FindPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("ko");
        user = FirebaseAuth.getInstance().getCurrentUser();
//
        firebaseAuth.signOut();
        //loginAuthQuery = new LoginAuthQuery(this);
        //loginAuthQuery.setSignOut();
//        TODO: [LoginActivity] 아래의 ActionBar 내용 확인 후 지우기
        // !!!!! ActionBar는 values-theme에서 수정하였음 !!!!!
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO: [LoginActivity] 편리한 테스트를 위한 주석처리
                // 아래 주석처리한 코드는 로그인 없이 바로 테스트하기 위함. 테스트할 때마다 작성하기 귀찮아서 놔둠
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                if (inputID.getText().toString().length() == 0 || inputPW.getText().toString().length() == 0) {
                    Toast.makeText(LoginActivity.this,"이메일, 패스워드를 전부 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    String email = inputID.getText().toString().trim();
                    String pw = inputPW.getText().toString().trim();
                    //loginAuthQuery.loginEmailAndPassword(email,pw,inputID,inputPW);
                    firebaseAuth.signInWithEmailAndPassword(email,pw)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (task.isSuccessful()) {
                                        if (user.isEmailVerified()) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this,"Not Vertiy Email",Toast.LENGTH_SHORT).show();
                                        }
                                         // 추가해도 될까?
                                    } else {
                                        if (user == null) {
                                            Toast.makeText(LoginActivity.this,"사용자 계정 없음",Toast.LENGTH_SHORT).show();
                                            inputID.setText("");
                                            inputPW.setText("");
                                            return;
                                        }
                                        else if (user != null) {
                                            //Toast.makeText(LoginActivity.this,"",Toast.LENGTH_SHORT).show();
                                            Toast.makeText(LoginActivity.this,"비밀번호 오류",Toast.LENGTH_SHORT).show();
                                            inputPW.setText("");
                                            firebaseAuth.signOut();
                                            return;
                                        } else {
                                            Toast.makeText(LoginActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                                            inputID.setText("");
                                            inputPW.setText("");
                                            return;
                                        }

                                    }
                                }
                            });
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,FindPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    // EditText가 아닌 다른 곳 터치 시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}