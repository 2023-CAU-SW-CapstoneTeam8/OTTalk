package com.example.ottalk;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText mName, mEmail, mPassword, mCheck;
    Button mRegisterBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);

        mName = findViewById(R.id.register_name);
        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        mCheck = findViewById(R.id.register_passwordcheck);

        mRegisterBtn = findViewById(R.id.registerbutton);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String passcheck = mCheck.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("이메일을 입력해주세요.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("비밀번호를 입력해주세요.");
                    return;
                }
                if (!password.equals(passcheck)) {
                    mCheck.setError("비밀번호를 확인해주세요.");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "가입을 환영합니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입을 다시 진행해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
