package com.aula.leontis.interfaces;

import com.aula.leontis.models.auth.AuthResponse;
import com.aula.leontis.models.auth.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthInterface {
        @POST("/login")
        Call<AuthResponse> login(@Body LoginRequest loginRequest);

}
