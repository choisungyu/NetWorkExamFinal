package com.csg.networkexamfinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.csg.networkexamfinal.models.Todo;
import com.csg.networkexamfinal.models.User;
import com.csg.networkexamfinal.ui.PhotoFragment;
import com.csg.networkexamfinal.ui.TodoListFragment;
import com.csg.networkexamfinal.ui.UserListFragment;

public class MainActivity extends AppCompatActivity implements UserListFragment.OnUserClickListener, TodoListFragment.OnTodoClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserListFragment userListFragment = new UserListFragment();
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, userListFragment)
                    .commit();
        }
    }

    @Override
    public void onUserClick(User user) {
        TodoListFragment fragment = TodoListFragment.newInstance(user.getId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onIdClick(User user) {
        PhotoFragment fragment = PhotoFragment.newInstance(user.getId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onTodoClick(Todo todo) {

    }
}
