package com.example.lostandfind.query.login;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.lostandfind.activity.login.FindPasswordActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordQuery {
    private FirebaseAuth auth;
    private Context context;
    public FindPasswordQuery(Context context) {
        auth = FirebaseAuth.getInstance();
        this.context = context;
    }
    public void setEmptyEmail() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("이메일 에러");
        dlg.setMessage("이메일을 입력해주시기 바랍니다.");
        dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG,"Developer Log: Empty Email");
            }
        });
        dlg.show();
    }
    public void setPasswordResetEmail(EditText etFindEmail) {
        String temp_email = etFindEmail.getText().toString();
        auth.sendPasswordResetEmail(temp_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                    dlg.setTitle("이메일 전송");
                    dlg.setMessage("가입하신 이메일로 비밀번호 재전송 메일이 발송되었습니다.");
                    dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(TAG,"Developer Message: Success reset Password Dialog");
                            ((FindPasswordActivity)context).finish();
                        }
                    });
                    dlg.show();
                } else {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                    dlg.setTitle("이메일 전송 에러");
                    dlg.setMessage("이메일을 다시 확인해주시기 바랍니다.");
                    dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(TAG,"Developer Message: Failed reset Password Dialog");
                        }
                    });
                    dlg.show();
                }
            }
        });
    }
}
