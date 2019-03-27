package com.csg.networkexamfinal.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csg.networkexamfinal.R;
import com.csg.networkexamfinal.api.JsonPlaceHolderService;
import com.csg.networkexamfinal.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserListFragment extends Fragment {

    private static final String TAG = UserListFragment.class.getSimpleName() + "!!!!";
    private UserRecyclerAdapter mAdapter;
    private OnUserClickListener mListener;
    private ProgressBar mProgressBar;


    public UserListFragment() {
    }

    public static UserListFragment newInstance() {
        return new UserListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.progressBar);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new UserRecyclerAdapter(mListener);
        recyclerView.setAdapter(mAdapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderService service = retrofit.create(JsonPlaceHolderService.class);

        service.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                mProgressBar.setVisibility(View.GONE);
                List<User> userList = response.body();
                Log.d(TAG, "onResponse: " + userList);

                mAdapter.setItems(userList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserClickListener) {
            mListener = (OnUserClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
        void onIdClick(User user);
    }

    static class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {


        private final OnUserClickListener mListener;
        private List<User> mItems = new ArrayList<>();

        public UserRecyclerAdapter(OnUserClickListener listener) {
            mListener = listener;
        }


        public void setItems(List<User> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
            final UserViewHolder viewHolder = new UserViewHolder(view);
            viewHolder.usernameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = mItems.get(viewHolder.getAdapterPosition());
                    mListener.onUserClick(user);
                }
            });
            viewHolder.idTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = mItems.get(viewHolder.getAdapterPosition());
                    mListener.onIdClick(user);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
            User user = mItems.get(i);

            userViewHolder.usernameTextView.setText(user.getUsername());
            userViewHolder.idTextView.setText(user.getId()+"");
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        static class UserViewHolder extends RecyclerView.ViewHolder {

            public TextView usernameTextView;
            public TextView idTextView;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                usernameTextView = itemView.findViewById(R.id.user_name_textView);
                idTextView = itemView.findViewById(R.id.user_id_textView);
            }
        }
    }
}
