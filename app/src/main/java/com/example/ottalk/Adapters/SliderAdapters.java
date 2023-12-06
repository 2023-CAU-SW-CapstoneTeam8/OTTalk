package com.example.ottalk.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.ottalk.Domain.SliderItems;
import com.example.ottalk.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class SliderAdapters extends RecyclerView.Adapter<SliderAdapters.SliderViewHolder> {
    private List<SliderItems> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdapters(List<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.context = viewPager2.getContext();
        loadDataFromFirestore(); // 데이터를 Firestore에서 가져오도록 수정
    }

    private void loadDataFromFirestore() {
        // Firestore에서 데이터를 가져오는 코드
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sliderItems.clear(); // 기존 데이터를 모두 지우고 새로운 데이터로 대체
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Firestore에서 가져온 각 문서를 SliderItems 객체로 변환하여 리스트에 추가
                            String imageUrl = document.getString("thumbnail");
                            SliderItems sliderItem = new SliderItems(imageUrl);
                            sliderItems.add(sliderItem);
                        }
                        notifyDataSetChanged(); // 데이터가 변경되었음을 알림
                    } else {
                        // 데이터 가져오기 실패 처리
                    }
                });
    }

    @NonNull
    @Override
    public SliderAdapters.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        context = parent.getContext();
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.slide_item_container,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapters.SliderViewHolder holder, int position){
        holder.setImage(sliderItems.get(position));
        if (position == sliderItems.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount(){
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public SliderViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SliderItems sliderItems){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(60));

            // Glide로 Firebase Firestore에서 이미지 URL을 가져와 로드
            Glide.with(context)
                    .load(sliderItems.getImageUrl()) // getImageUrl()는 SliderItems에서 이미지 URL을 반환하는 메서드로 가정
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}

