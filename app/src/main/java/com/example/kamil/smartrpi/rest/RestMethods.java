package com.example.kamil.smartrpi.rest;

import com.example.kamil.smartrpi.models.messages.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestMethods {

    @POST("register")
    Call<String> registerUser(@Body UserModel user);

    @POST("login")
    Call<String> loginUser(@Body UserModel user);
}