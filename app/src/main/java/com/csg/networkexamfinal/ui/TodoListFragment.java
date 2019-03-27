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
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csg.networkexamfinal.R;
import com.csg.networkexamfinal.api.JsonPlaceHolderService;
import com.csg.networkexamfinal.models.Todo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoListFragment extends Fragment {
    private static final String ARG_USER_ID = "userid";

    private int mUserId;
    private static final String TAG = TodoListFragment.class.getSimpleName() + "!!!!";
    private TodoRecyclerAdapter mAdapter;
    private OnTodoClickListener mListener;
    private ProgressBar mProgressBar;


    public TodoListFragment() {
    }

    public static TodoListFragment newInstance(int userId) {
        TodoListFragment fragment = new TodoListFragment();
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
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.progressBar);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new TodoListFragment.TodoRecyclerAdapter(mListener);
        recyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderService service = retrofit.create(JsonPlaceHolderService.class);

        service.getTodos().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                mProgressBar.setVisibility(View.GONE);
                List<Todo> todoList = response.body();
                Log.d(TAG, "onResponse: " + todoList);

                mAdapter.setItems(todoList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTodoClickListener) {
            mListener = (OnTodoClickListener) context;
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

    public interface OnTodoClickListener {
        void onTodoClick(Todo todo);
    }

    static class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder> {


        private final OnTodoClickListener mListener;
        private List<Todo> mItems = new ArrayList<>();

        public TodoRecyclerAdapter(OnTodoClickListener listener) {
            mListener = listener;
        }


        public void setItems(List<Todo> items) {
            mItems = items;
        }


        @NonNull
        @Override
        public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_todo, viewGroup, false);
            final TodoViewHolder viewHolder = new TodoViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Todo todo = mItems.get(viewHolder.getAdapterPosition());
                    mListener.onTodoClick(todo);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TodoViewHolder todoViewHolder, int i) {
            Todo todo = mItems.get(i);
            todoViewHolder.titleTextView.setText(todo.getTitle());
            todoViewHolder.checkBox.setChecked(todo.isCompleted());
            todoViewHolder.checkBox.setEnabled(false);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        static class TodoViewHolder extends RecyclerView.ViewHolder {

            public TextView titleTextView;
            public CheckBox checkBox;

            public TodoViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.title);
                checkBox = itemView.findViewById(R.id.checkBox);
            }
        }
    }
}
