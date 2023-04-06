package com.example.lostandfind.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.login.LoginActivity;
import com.example.lostandfind.activity.my.ModifyNickActivity;
import com.example.lostandfind.activity.my.MyPost;
import com.example.lostandfind.query.myfragment.MyFragementQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyFragment extends Fragment {
    ViewGroup rootView;

    Button modify_nick_btn;
    TextView notice_btn;

    TextView modify_pw_btn;
    TextView setup_alert_btn;
    TextView setup_disturb_btn;

    TextView logout_btn;
    TextView delete_account_btn;
    TextView tvMyName,tvMyEmail;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    MyFragementQuery dao;
    /*FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my, container, false);
        tvMyName = (TextView)rootView.findViewById(R.id.tvMyName);
        tvMyEmail = (TextView)rootView.findViewById(R.id.tvMyEmail);

        dao = new MyFragementQuery();
//        DocumentReference DRef = db.collection("Users").document(user.getUid());
//        DRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    tvMyName.setText(document.toObject(UserData.class).getName());
//                    tvMyEmail.setText(document.toObject(UserData.class).getEmail());
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG,"Developer: User Error: ",e);
//            }
//        });
        dao.setMyFragmentUserData(tvMyName,tvMyEmail);
        // 공지사항을 누르면, 공지사항에 대한 페이지 실행
        notice_btn = rootView.findViewById(R.id.notice_btn);
        notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyPost.class);
                startActivity(intent);
            }
        });

        // 닉네임 수정 버튼을 누르면, 닉네임을 수정하는 페이지 실행
        modify_nick_btn = rootView.findViewById(R.id.modify_nick_btn);
        modify_nick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModifyNickActivity.class);
                intent.putExtra("name",tvMyName.getText().toString());
                startActivity(intent);
            }
        });

        // 비밀번호 변경 누르면, 비밀번호 변경에 대한 페이지 실행
        modify_pw_btn = rootView.findViewById(R.id.modify_pw_btn);
        modify_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("비밀번호 변경").setMessage("해당 이메일로 비밀번호 변경 메일이 전송되었습니다.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                auth.signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText
                                        (getActivity(),
                                                "로그아웃 되었습니다.",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                }).show();
//                Intent intent = new Intent(getActivity(), ModifyPwActivity.class);
//                startActivity(intent);
            }
        });

        // 알림 설정 누르면, 알림 설정에 대한 페이지 실행
        //setup_alert_btn = rootView.findViewById(R.id.setup_alert_btn);
//        setup_alert_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), SetupAlertActivity.class);
//                startActivity(intent);
//            }
//        });

        // 방해금지 시간 설정 누르면, 방해금지 시간을 설정할 수 있는 페이지 실행
        //setup_disturb_btn = rootView.findViewById(R.id.setup_disturb_btn);
//        setup_disturb_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), SetupDisturbActivity.class);
//                startActivity(intent);
//            }
//        });

        // [완료] 로그아웃 처리
        logout_btn = rootView.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("로그아웃 알림")
                        .setMessage("정말 로그아웃 하시겠어요?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                auth.signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText
                                        (getActivity(),
                                        "안녕히 가세요!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        // 탈퇴하기 페이지 이동
        //delete_account_btn = rootView.findViewById(R.id.delete_account_btn);
//        delete_account_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), DeleteAccountActivity.class);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }
}