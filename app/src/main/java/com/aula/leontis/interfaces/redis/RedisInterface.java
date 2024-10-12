package com.aula.leontis.interfaces.redis;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RedisInterface {
    @POST("/api/usuarios/atividade/incrementar")
    Call<ResponseBody> incrementarAtividadeUsuario();

    @POST("/api/usuarios/atividade/decrementar")
    Call<ResponseBody> decrementarAtividadeUsuario();

    @POST("/api/estatisticas/visualizacao/{obraId}")
    Call<ResponseBody> incrementarVisualizacaoObra(@Path("obraId") long obraId);
}
