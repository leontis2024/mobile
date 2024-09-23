package com.aula.leontis.interfaces.obra;

import com.aula.leontis.models.obra.Obra;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ObraInterface {
    @GET("/api/obra/selecionarPorMuseu/{id}")
    Call<List<Obra>> selecionarObrasPorMuseu(@Path("id") String id);
}
