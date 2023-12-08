package com.example.ottalk;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.ottalk.Domain.Contents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTxt, contentRateTxt, contentTimeTxt, contentSummaryInfo;
    private ImageView contentPoster;

    private Contents content;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // movieTitleTextView 초기화
        TextView movieTitleTextView = findViewById(R.id.contentsNameTxt);

        ImageView goBackButton = findViewById(R.id.goback_detailbtn);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView fav_detailbtn = findViewById(R.id.fav_detailbtn);
        fav_detailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // movieTitleTextView가 null이 아닌지 확인하고 toString 호출
                if (movieTitleTextView != null) {
                    String contentstitleTxt = movieTitleTextView.getText().toString();
                    addToShelf(contentstitleTxt);
                }
            }
        });

        content = getIntent().getParcelableExtra("content");
        if (content != null) {
            initView();
            displayContentDetails();
        } else {
            Toast.makeText(this, "Content not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        titleTxt = findViewById(R.id.contentsNameTxt);
        contentPoster = findViewById(R.id.contentPoster);
        contentRateTxt = findViewById(R.id.contentsStar);
        contentTimeTxt = findViewById(R.id.contentsTime);
        contentSummaryInfo = findViewById(R.id.contentsSummary);
    }

    private void addToShelf(String movieTitle) {
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();

            // 현재 사용자의 UID에 해당하는 문서를 참조
            DocumentReference userDocRef = db.collection("users").document(uid);

            // 해당 문서의 "shelf" 필드에 영화 제목 추가
            userDocRef.update("shelf", FieldValue.arrayUnion(movieTitle))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailActivity.this, "성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Completed method: Display details of the Contents object in the views
    private void displayContentDetails() {
        titleTxt.setText(content.getName());
        contentRateTxt.setText(content.getRateStar());
        contentTimeTxt.setText(content.getTime());
        contentSummaryInfo.setText(content.getSynopsis());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(this)
                .load(content.getImage())
                .apply(requestOptions)
                .into(contentPoster);
    }
}

