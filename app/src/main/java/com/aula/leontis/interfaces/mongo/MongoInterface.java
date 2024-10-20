package com.aula.leontis.interfaces.mongo;
import com.aula.leontis.models.avaliacao.Avaliacao;
import com.aula.leontis.models.comentario.Comentario;
import com.aula.leontis.models.comentario.ComentarioResponse;
import com.aula.leontis.models.historico.Historico;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MongoInterface {
    @PUT("/api/usuarios/{userId}/comentarios")
    Call<ResponseBody> inserirComentario(@Path("userId") long userId, @Body Comentario comentario);

    @PUT("/api/usuarios/{userId}/avaliacoes")
    Call<ResponseBody> inserirAvaliacao(@Path("userId") long userId, @Body Avaliacao avaliacao);
    @PUT("/api/usuarios/{userId}/historico-obras")
    Call<ResponseBody> inserirHistorico(@Path("userId") long userId, @Body Historico historico);

    @GET("/api/usuarios/comentarios/{obraId}")
    Call<List<ComentarioResponse>> buscarComentariosPorIdObra(@Path("obraId") long obraId);
    @GET("api/usuarios/{userId}/historicoObras")
    Call<List<Historico>> buscarHistoricoPorId(@Path("userId") long userId);
    @GET("/api/usuarios/media-nota/{obraId}")
    Call<ResponseBody> buscarMediaNotaPorIdObra(@Path("obraId") long obraId);
    @GET("/api/usuarios/porcentagem")
    Call<ResponseBody> buscarPorcentagem(@Query("obraId") long obraId, @Query("notaMinima") double notaMinima, @Query("notaMaxima") double notaMaxima);
}

