package com.example.ottalk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MypageFragment extends Fragment {

    private TextView profileName, profileEmail, titleState;
    Button editButton_mypage;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypage, container, false);

        profileName = rootView.findViewById(R.id.profileName);
        profileEmail = rootView.findViewById(R.id.profileEmail);
        titleState = rootView.findViewById(R.id.user_state);
        editButton_mypage = rootView.findViewById(R.id.editButton_mypage);

        mAuth = FirebaseAuth.getInstance();

        showUserData();

        editButton_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passUserData();
            }
        });

        return rootView;
    }

    private void showUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String nameUser = user.getUid();
            String emailUser = user.getEmail();

            titleState.setText("Hello World");
            profileEmail.setText(emailUser);
            profileName.setText(nameUser);
        }
    }



}
