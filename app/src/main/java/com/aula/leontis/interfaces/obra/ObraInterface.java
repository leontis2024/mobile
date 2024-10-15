package com.aula.leontis.interfaces.obra;

import com.aula.leontis.models.obra.Obra;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ObraInterface {
    @GET("/api/obra/selecionarObraPorID/{id}")
    Call<Obra> selecionarObraPorId(@Path("id") long id);
    @GET("/api/obra/selecionarPorMuseu/{id}")
    Call<List<Obra>> selecionarObrasPorMuseu(@Path("id") long id);
    @GET("/api/obra/selecionarPorGenero/{id}")
    Call<List<Obra>> selecionarObrasPorGenero(@Path("id") long id);
    @GET("/api/obra/selecionarPorArtista/{id}")
    Call<List<Obra>> selecionarObrasPorArtista(@Path("id") long id);
    @GET("/api/obra/selecionarPorGeneros")
    Call<List<Obra>> selecionarObrasPorVariosGeneros(@Query("generos") List<Long> generos);
    @GET("/api/obra/selecionarPorMuseus")
    Call<List<Obra>> selecionarObrasPorVariosMuseus(@Query("museus") List<Long> museus);


}
