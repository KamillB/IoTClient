package com.example.kamil.smartrpi.rest;

import com.example.kamil.smartrpi.models.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestMethods {

    @POST("register")
    Call<String> registerUser(@Body UserModel user);

    @POST("login")
    Call<String> loginUser(@Body UserModel user);
}