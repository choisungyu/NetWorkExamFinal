package com.csg.networkexamfinal.api;

import com.csg.networkexamfinal.models.Photo;
import com.csg.networkexamfinal.models.Todo;
import com.csg.networkexamfinal.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderService {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("photos")
    Call<List<Photo>> listPhotos();

    @GET("todos")
    Call<List<Todo>> getTodos();
}
