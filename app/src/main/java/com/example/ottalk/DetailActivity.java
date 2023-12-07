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

public class DetailActivity extends AppCompatActivity {

    private TextView titleTxt, contentRateTxt, contentTimeTxt, contentSummaryInfo;
    private ImageView contentPoster;

    private Contents content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView goBackButton = findViewById(R.id.goback_detailbtn);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

