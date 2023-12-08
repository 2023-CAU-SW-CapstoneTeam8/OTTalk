package com.example.ottalk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ottalk.Adapters.FilmListAdapter;
import com.example.ottalk.Domain.Contents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    private List<Contents> wishlist;
    private FilmListAdapter adapter;
    private String currentUserUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWish);
        wishlist = new ArrayList<>();
        adapter = new FilmListAdapter(wishlist);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentUserUid = auth.getCurrentUser().getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(currentUserUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Contents userFavorites = task.getResult().toObject(Contents.class);
                            List<String> favoriteMovies = userFavorites != null ? userFavorites.getFavoriteMovie() : null;

                            if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
                                db.collection("movies1")
                                        .whereIn("name", favoriteMovies)
                                        .get()
                                        .addOnCompleteListener(moviesTask -> {
                                            if (moviesTask.isSuccessful()) {
                                                wishlist.clear();
                                                for (QueryDocumentSnapshot document : moviesTask.getResult()) {
                                                    Contents content = document.toObject(Contents.class);
                                                    wishlist.add(content);
                                                }
                                                adapter.setItems(wishlist);
                                            }
                                        });
                            }
                        }
                    });
        }

        return view;
    }
}
