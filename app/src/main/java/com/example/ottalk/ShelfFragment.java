package com.example.ottalk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ottalk.Adapters.FilmListAdapter;
import com.example.ottalk.Domain.Contents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShelfFragment extends Fragment {

    private RecyclerView recyclerViewShelf;
    private FilmListAdapter filmListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelf, container, false);

        recyclerViewShelf = view.findViewById(R.id.recyclerViewShelf);
        recyclerViewShelf.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewShelf.setAdapter(filmListAdapter);

        // FilmListAdapter를 초기화하고 RecyclerView에 설정
        filmListAdapter = new FilmListAdapter(new ArrayList<>());
        recyclerViewShelf.setAdapter(filmListAdapter);

        // Firestore에서 사용자 정보 및 shelf 정보 가져오기
        loadUserShelf();

        return view;
    }

    // Firestore에서 사용자 정보 및 shelf 정보 가져오는 메서드
    private void loadUserShelf() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Firestore에서 사용자 정보 가져오기
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("users").document(uid);

            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // 사용자 정보를 가져온 후 shelf 정보를 filmListAdapter에 설정
                    List<String> shelfItems = (List<String>) documentSnapshot.get("shelf");
                    if (shelfItems != null && !shelfItems.isEmpty()) {
                        // shelfItems에 있는 각각의 영화 제목에 해당하는 Contents 객체를 가져오기
                        getContentsByTitles(shelfItems);
                    }
                }
            }).addOnFailureListener(e -> {
                // 실패 시 처리
                Toast.makeText(requireContext(), "사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // 영화 제목에 해당하는 Contents 객체들을 가져와서 FilmListAdapter에 추가하는 메서드
    private void getContentsByTitles(List<String> movieTitles) {
        List<Contents> contentsList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String title : movieTitles) {
            // "movies1" 컬렉션에서 영화 제목에 해당하는 문서 가져오기
            db.collection("movies1")
                    .whereEqualTo("name", title)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Contents content = documentSnapshot.toObject(Contents.class);
                            contentsList.add(content);
                        }
                        // Adapter에 변경된 데이터를 알리고 갱신
                        filmListAdapter.setItems(contentsList);
                    })
                    .addOnFailureListener(e -> {
                        // 실패 시 처리
                        Toast.makeText(requireContext(), "영화 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
