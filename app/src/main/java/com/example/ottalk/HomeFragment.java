package com.example.ottalk;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ottalk.Adapters.FilmListAdapter;
import com.example.ottalk.Adapters.SliderAdapters;
import com.example.ottalk.Domain.Contents;
import com.example.ottalk.Domain.SliderItems;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    private FirebaseFirestore firestore;
    private CollectionReference moviesCollection;
    private FilmListAdapter adapterHotContents;
    private RecyclerView recyclerViewHotContents;
    private ProgressBar loading1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        banners();

        firestore = FirebaseFirestore.getInstance();
        moviesCollection = firestore.collection("movies1");

        sendRequest();

        TextView textViewNew = view.findViewById(R.id.textView_new);
        textViewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new CommunityFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void sendRequest() {
        loading1.setVisibility(View.VISIBLE);

        moviesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Contents> contentsList = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Contents content = document.toObject(Contents.class);
                    String id = createDocumentId(content.getName());
                    content.setId(id);
                    contentsList.add(content);
                }

                Collections.shuffle(contentsList);

                List<Contents> randomContentsList = contentsList.subList(0, Math.min(contentsList.size(), 5));

                loading1.setVisibility(View.GONE);
                adapterHotContents = new FilmListAdapter(randomContentsList);
                recyclerViewHotContents.setAdapter(adapterHotContents);
            } else {
                loading1.setVisibility(View.GONE);
                Log.i("UI", "Error: " + task.getException().toString());
            }
        });
    }

    private String createDocumentId(String name) {
        if (name == null) {
            return "";
        }

        return name.replaceAll("[^A-Za-z0-9]", "");
    }

    private void initView(View view) {
        viewPager2 = view.findViewById(R.id.viewpagerSlider);
        recyclerViewHotContents = view.findViewById(R.id.view_HotContents);
        recyclerViewHotContents.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        loading1 = view.findViewById(R.id.progressBar1);
    }

    private void banners() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide2));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
            }
        });

        Runnable sliderRunnable = () -> viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        slideHandler.postDelayed(sliderRunnable, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 2000);
    }

    private Runnable sliderRunnable = () -> viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);

}

