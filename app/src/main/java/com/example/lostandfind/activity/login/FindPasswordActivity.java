package com.example.lostandfind.activity.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.query.login.FindPasswordQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordActivity extends AppCompatActivity {
    FindPasswordQuery findPasswordQuery;
    EditText etFindEmail;
    Button btnFindEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        findPasswordQuery = new FindPasswordQuery(this);

        etFindEmail = (EditText) findViewById(R.id.etFindEmail);
        btnFindEmail = (Button) findViewById(R.id.btnFindEmail);
        btnFindEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etFindEmail.getText().toString().equals("")) {
                    findPasswordQuery.setEmptyEmail();
                }else {
                    findPasswordQuery.setPasswordResetEmail(etFindEmail);
                }
            }
        });
    }
}