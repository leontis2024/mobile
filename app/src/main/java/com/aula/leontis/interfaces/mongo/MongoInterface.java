package com.aula.leontis.interfaces.mongo;
import com.aula.leontis.models.avaliacao.Avaliacao;
import com.aula.leontis.models.comentario.Comentario;
import com.aula.leontis.models.comentario.ComentarioResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MongoInterface {
    @PUT("/api/usuarios/{userId}/comentarios")
    Call<ResponseBody> inserirComentario(@Path("userId") long userId, @Body Comentario comentario);

    @PUT("/api/usuarios/{userId}/avaliacoes")
    Call<ResponseBody> inserirAvaliacao(@Path("userId") long userId, @Body Avaliacao avaliacao);

    @GET("/api/usuarios/comentarios/{obraId}")
    Call<List<ComentarioResponse>> buscarComentariosPorIdObra(@Path("obraId") long obraId);
    @GET("/api/usuarios/media-nota/{obraId}")
    Call<ResponseBody> buscarMediaNotaPorIdObra(@Path("obraId") long obraId);
}

