package com.aula.leontis.interfaces.museu;

import com.aula.leontis.models.museu.Museu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MuseuInterface {
    @GET("/api/museu/selecionarTudo")
    Call<List<Museu>> selecionarTodosMuseus();
}
