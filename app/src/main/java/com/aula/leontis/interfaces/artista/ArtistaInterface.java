package com.aula.leontis.interfaces.artista;

import com.aula.leontis.models.artista.Artista;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ArtistaInterface {

    @GET("/api/artista/selecionarPorID/{id}")
    Call<Artista> buscarArtistaPorId(@Path("id") long id);
}
