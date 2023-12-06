package com.example.ottalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.ottalk.Adapters.SliderAdapters;
import com.example.ottalk.Domain.SliderItems;
import com.example.ottalk.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    //backend
    private RecyclerView.Adapter adapterHotContents;
    private RecyclerView recyclerViewHotContents;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar loading1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getContext().getTheme().applyStyle(R.style.Theme_Ottalk, true);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        banners();

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

    private void initView(View view) {
        viewPager2 = view.findViewById(R.id.viewpagerSlider);
        recyclerViewHotContents = view.findViewById(R.id.view_HotContents);
        recyclerViewHotContents.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        loading1 = view.findViewById(R.id.progressBar1);
    }

    private void banners() {
        List<SliderItems> sliderItems = new ArrayList<>();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // 예시: Firestore에서 Movies 컬렉션의 문서들을 가져와서 SliderItems 객체를 생성
        firestore.collection("Movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // "thumbnail" 필드에 저장된 이미지 URL을 가져와 SliderItems 객체를 생성
                            String imageUrl = document.getString("썸네일");
                            SliderItems sliderItem = new SliderItems(imageUrl);

                            // SliderItems 객체를 리스트에 추가
                            sliderItems.add(sliderItem);
                        }

                        // 데이터가 변경되었음을 어댑터에 알림
                        // adapter.notifyDataSetChanged();
                    }
                });

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

