package com.example.ottalk;


// 필요한 추가 import 문

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.Arrays;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class RegisterActivity extends AppCompatActivity {
    EditText mName, mEmail, mPassword, mCheck;
    EditText mFavoriteMovie; // 추가 정보 입력 필드
    Button mRegisterBtn;
    FirebaseAuth fAuth;

    private Spinner spinnerGender, spinnerAge, spinnerMBTI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);

        mName = findViewById(R.id.register_name);
        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        mCheck = findViewById(R.id.register_passwordcheck);

        // 추가 정보 입력 필드 연결
        // 성별 Spinner 설정
        Spinner spinnerGender = findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapterGender = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, getResources().getStringArray(R.array.gender_array));
        adapterGender.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerGender.setAdapter(adapterGender);

        // 연령대 Spinner 설정
        Spinner spinnerAge = findViewById(R.id.spinner_age);
        ArrayAdapter<CharSequence> adapterAge = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, getResources().getStringArray(R.array.age_array));
        adapterAge.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerAge.setAdapter(adapterAge);

        // MBTI Spinner 설정
        Spinner spinnerMBTI = findViewById(R.id.spinner_mbti);
        ArrayAdapter<CharSequence> adapterMBTI = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, getResources().getStringArray(R.array.mbti_array));
        adapterMBTI.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerMBTI.setAdapter(adapterMBTI);

        mFavoriteMovie = findViewById(R.id.register_favorite_movie);

        mRegisterBtn = findViewById(R.id.registerbutton);

        fAuth = FirebaseAuth.getInstance();

        TextView textViewGotoRegister = findViewById(R.id.textView_gotoLogin);
        textViewGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String passcheck = mCheck.getText().toString().trim();
                String favoriteMoviesInput = mFavoriteMovie.getText().toString().trim();
                String[] favoriteMoviesArray = favoriteMoviesInput.split(",");


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

                            // 사용자 추가 정보 가져오기
                            String gender = spinnerGender.getSelectedItem().toString();
                            String age = spinnerAge.getSelectedItem().toString();
                            String mbti = spinnerMBTI.getSelectedItem().toString();
                            String favoriteMovie = mFavoriteMovie.getText().toString().trim();

                            // Firestore에 사용자 추가 정보 저장
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("gender", gender);
                                userInfo.put("age", age);
                                userInfo.put("mbti", mbti);
                                userInfo.put("favoriteMovie", Arrays.asList(favoriteMoviesArray));

                                db.collection("users").document(firebaseUser.getUid()).set(userInfo);
                            }


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
