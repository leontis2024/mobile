package com.aula.leontis.interfaces;

import com.aula.leontis.models.auth.AuthResponse;
import com.aula.leontis.models.auth.LoginRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Query;

public interface AuthInterface {

        @POST("/api/auth/login")
        Call<Map<String, String>> login(@Body LoginRequest loginRequest);

        @POST("/api/auth/refresh")
        Call<Map<String, String>> refreshToken(@Body Map<String, String> tokens);
}
