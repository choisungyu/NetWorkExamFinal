package com.csg.networkexamfinal.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.csg.networkexamfinal.R;
import com.csg.networkexamfinal.api.JsonPlaceHolderService;
import com.csg.networkexamfinal.databinding.ItemPhotoBinding;
import com.csg.networkexamfinal.models.Photo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PhotoFragment extends Fragment {

    private static final String ARG_USER_ID = "userId";

    private int mUserId;
    private ProgressBar mProgressBar;
    private PhotoRecyclerAdapter mAdapter;

    private static final String TAG = PhotoFragment.class.getSimpleName() + "!!!!";

    public PhotoFragment() {
    }

    public static PhotoFragment newInstance(int userId) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getInt(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        mProgressBar = view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        mAdapter = new PhotoRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderService service = retrofit.create(JsonPlaceHolderService.class);

        service.listPhotos().enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                mProgressBar.setVisibility(View.GONE);
                List<Photo> photoList = response.body();
                Log.d(TAG, "onResponse: " + photoList);

                mAdapter.setItems(photoList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
        return view;
    }

    static class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder> {

        private List<Photo> mItems = new ArrayList<>();

        public void setItems(List<Photo> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, viewGroup, false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int i) {
//            Photo photo = ;
            photoViewHolder.binding.setPhoto(mItems.get(i));
//            Glide.with(photoViewHolder.itemView)
//                    .load(photo.getThumbnailUrl())
//                    .centerCrop()
//                    .placeholder(R.mipmap.ic_launcher)
//                    .into(photoViewHolder.thumbnailImageView);

//            photoViewHolder.titleTextView.setText(photo.getTitle());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        static class PhotoViewHolder extends RecyclerView.ViewHolder {

//            public ImageView thumbnailImageView;
//            public TextView titleTextView;
            public ItemPhotoBinding binding;

            public PhotoViewHolder(@NonNull View itemView) {
                super(itemView);
//                thumbnailImageView = itemView.findViewById(R.id.thumbnail_image_view);
//                titleTextView = itemView.findViewById(R.id.title_text_view);

                binding = DataBindingUtil.bind(itemView);
            }
        }
    }

}
