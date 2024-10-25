package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;

import com.aula.leontis.Geral;
import com.aula.leontis.interfaces.redis.RedisInterface;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedisService {
    String urlAPI = Geral.getInstance().getUrlApiRedis();

    public void incrementarAtividadeUsuario() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedisInterface redisInterface = retrofit.create(RedisInterface.class);
        Call<ResponseBody> call = redisInterface.incrementarAtividadeUsuario();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("REDIS_API_RESPONSE_POST_INC_ATV", "Inserido " + body);
                } else {
                    try {
                       
                        String errorBody = response.errorBody().string();
                        Log.e("REDIS_API_ERROR_POST_INC_ATV", "Erro  " + response.code() + " - " + errorBody + " - " + response.message());
                                          } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REDIS_API_ERROR_POST_INC_ATV", "Erro");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("REDIS_API_ERROR_POST_INC_ATV", "Erro " + throwable.getMessage());
            }

        });
    }

    public void decrementarAtividadeUsuario() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedisInterface redisInterface = retrofit.create(RedisInterface.class);
        Call<ResponseBody> call = redisInterface.decrementarAtividadeUsuario();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("REDIS_API_RESPONSE_POST_DEC_ATV", "Inserido" + body);
                } else {
                    try {

                        String errorBody = response.errorBody().string();
                        Log.e("REDIS_API_ERROR_POST_DEC_ATV", "Erro  " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REDIS_API_ERROR_POST_DEC_ATV", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("REDIS_API_ERROR_POST_DEC_ATV", "Erro " + throwable.getMessage());
            }

        });
    }

    public void incrementarVizualizacao(String obraId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedisInterface redisInterface = retrofit.create(RedisInterface.class);
        Call<ResponseBody> call = redisInterface.incrementarVisualizacaoObra(Long.parseLong(obraId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("REDIS_API_RESPONSE_POST_INC_VIZUALIZACAO", "Inserido" + body);
                } else {
                    try {

                        String errorBody = response.errorBody().string();
                        Log.e("REDIS_API_ERROR_POST_INC_VIZUALIZACAO", "Erro  " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REDIS_API_ERROR_POST_INC_VIZUALIZACAO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("REDIS_API_ERROR_POST_INC_VIZUALIZACAO", "Erro " + throwable.getMessage());
            }

        });
    }

    public void incrementarAvaliacaoObra(String obraId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedisInterface redisInterface = retrofit.create(RedisInterface.class);
        Call<ResponseBody> call = redisInterface.incrementarNotaObra(Long.parseLong(obraId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("REDIS_API_RESPONSE_POST_INC_AVALIACAO", "Inserido" + body);
                } else {
                    try {

                        String errorBody = response.errorBody().string();
                        Log.e("REDIS_API_ERROR_POST_INC_AVALIACAO", "Erro  " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REDIS_API_ERROR_POST_INC_AVALIACAO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("REDIS_API_ERROR_POST_INC_AVALIACAO", "Erro " + throwable.getMessage());
            }

        });
    }
    public void incrementarComentarioObra(String obraId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedisInterface redisInterface = retrofit.create(RedisInterface.class);
        Call<ResponseBody> call = redisInterface.incrementarComentariosObra(Long.parseLong(obraId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("REDIS_API_RESPONSE_POST_INC_COMENTARIO", "Inserido" + body);
                } else {
                    try {

                        String errorBody = response.errorBody().string();
                        Log.e("REDIS_API_ERROR_POST_INC_COMENTARIO", "Erro  " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REDIS_API_ERROR_POST_INC_COMENTARIO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("REDIS_API_ERROR_POST_INC_COMENTARIO", "Erro " + throwable.getMessage());
            }

        });
    }


    public void decrementarAvaliacaoObra(String obraId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedisInterface redisInterface = retrofit.create(RedisInterface.class);
        Call<ResponseBody> call = redisInterface.decrementarNotaObra(Long.parseLong(obraId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("REDIS_API_RESPONSE_POST_DECR_AVALIACAO", "Inserido" + body);
                } else {
                    try {

                        String errorBody = response.errorBody().string();
                        Log.e("REDIS_API_ERROR_POST_DECR_AVALIACAO", "Erro  " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REDIS_API_ERROR_POST_DECR_AVALIACAO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("REDIS_API_ERROR_POST_DECR_AVALIACAO", "Erro " + throwable.getMessage());
            }

        });
    }
    public void decrementarComentarioObra(String obraId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedisInterface redisInterface = retrofit.create(RedisInterface.class);
        Call<ResponseBody> call = redisInterface.decrementarComentariosObra(Long.parseLong(obraId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                if (response.isSuccessful()) {
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("REDIS_API_RESPONSE_POST_DECR_COMENTARIO", "Inserido" + body);
                } else {
                    try {

                        String errorBody = response.errorBody().string();
                        Log.e("REDIS_API_ERROR_POST_DECR_COMENTARIO", "Erro  " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("REDIS_API_ERROR_POST_DECR_COMENTARIO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("REDIS_API_ERROR_POST_DECR_COMENTARIO", "Erro " + throwable.getMessage());
            }

        });
    }
}
