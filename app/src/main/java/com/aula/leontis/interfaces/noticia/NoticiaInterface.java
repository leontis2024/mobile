package com.aula.leontis.interfaces.noticia;

import com.aula.leontis.models.noticia.NewsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NoticiaInterface {
    @GET("everything")
    Call<NewsApiResponse> callHeadlines(
            @Query("language") String language,
            @Query("q") String q,
            @Query("apiKey") String api_key,
            @Query("sortBy") String sortBy
    );
}
