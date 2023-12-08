package com.example.ottalk;

import android.content.Intent;
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

public class MypageFragment extends Fragment {

    private TextView profileName, profileEmail, titleState;
    private Button logoutButton;;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypage, container, false);

        profileName = rootView.findViewById(R.id.profileName);
        profileEmail = rootView.findViewById(R.id.profileEmail);
        titleState = rootView.findViewById(R.id.user_state);
        logoutButton = rootView.findViewById(R.id.logout_mypage);

        mAuth = FirebaseAuth.getInstance();

        showUserData();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the user
                mAuth.signOut();
                // Redirect to LoginActivity
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish(); // Optional: finish the current activity
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
