package com.example.lostandfind.activity.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lostandfind.R;

public class MainChattingActivity extends AppCompatActivity {
    Intent intent;
    String postCreaterEmail;
    TextView testOnly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chatting);
        intent = getIntent();
        postCreaterEmail = (String)intent.getSerializableExtra("user_email");
    }
}