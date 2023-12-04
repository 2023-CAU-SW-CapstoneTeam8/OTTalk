package com.example.ottalk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textViewGotoRegister = findViewById(R.id.textView_gotoRegister);
        textViewGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //원래는 회원가입으로 가야하는데, 버그 터지는 이유를 모르겠음
                //startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}