package com.aula.leontis.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.interfaces.usuario.UsuarioGeneroInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.usuario.Usuario;
import com.aula.leontis.models.usuario.UsuarioGenero;
import com.aula.leontis.models.usuario.UsuarioMongo;
import com.aula.leontis.utilities.MetodosAux;
import com.bumptech.glide.Glide;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioService {
    MetodosAux aux = new MetodosAux();
    String urlAPI = Geral.getInstance().getUrlApiSql();

    public void selecionarUsuarioPorId(String id, Context context, TextView apelido, TextView biografia, ImageView foto, TextView nome, TextView sobrenome, TextView telefone, TextView sexo, TextView dtNasc, TextView erro) {
        erro.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erro.setText("Carregando...");
        erro.setVisibility(View.VISIBLE);
        ApiService apiService = new ApiService(context);
        UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
        Call<ResponseBody> call = usuarioInterface.selecionarUsuarioPorID(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        erro.setVisibility(View.INVISIBLE);
                        String jsonResponse = response.body().string();

                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        String apelidoApi = jsonObject.getString("apelido");
                        String nomeApi = jsonObject.getString("nome");
                        String biografiaApi = jsonObject.getString("biografia");
                        String urlFotoApi = jsonObject.getString("urlImagem");
                        String sobrenomeApi = jsonObject.getString("sobrenome");
                        String telefoneApi = jsonObject.getString("telefone");
                        String sexoApi = jsonObject.getString("sexo");
                        String dtNascApi = jsonObject.getString("dataNascimento");

                        if (urlFotoApi == null) {
                            urlFotoApi = "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png";
                        }
                        apelido.setText(apelidoApi);
                        biografia.setText(biografiaApi);
                        nome.setText(nomeApi);
                        sobrenome.setText(sobrenomeApi);
                        telefone.setText(telefoneApi);
                        LocalDate data = LocalDate.parse(dtNascApi);
                        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String dataFormatada = data.format(formatador);
                        dtNasc.setText(dataFormatada);
                        sexo.setText(sexoApi);
                        Glide.with(context).load(urlFotoApi).circleCrop().into(foto);

                        // Faça algo com os valores obtidos
                        Log.d("API_RESPONSE_GET_ID_USUARIO", "Campos obtidos: apelido: " + apelidoApi + " nome: " + nomeApi + " biografia: " + biografiaApi + " urlFoto: " + urlFotoApi + " sobrenome: " + sobrenomeApi + " telefone: " + telefoneApi + " sexo: " + sexoApi + " dtNasc: " + dtNascApi);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_GET_ID_USUARIO", "Erro ao processar resposta: " + e.getMessage());
                        erro.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        erro.setText("Não possivel trazer os dados do perfil");
                        erro.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("API_ERROR_GET_ID_USUARIO", "Erro na resposta da API: " + response.code());
                    erro.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    erro.setText("Não possivel trazer os dados do perfil");
                    erro.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_GET_ID_USUARIO", "Erro ao fazer a requisição: " + throwable.getMessage());
                erro.setText("Erro inesperado");
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter dados do perfil\nMensagem: " + throwable.getMessage());
            }
        });
    }


    public void selecionarUsuarioPorIdParcial(String id, Context context, ImageView foto, TextView nome) {
        ApiService apiService = new ApiService(context);
        UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
        Call<ResponseBody> call = usuarioInterface.selecionarUsuarioPorID(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonResponse = response.body().string();

                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        String nomeApi = jsonObject.getString("nome");
                        String apelidoApi = jsonObject.getString("apelido");
                        String urlFotoApi = jsonObject.getString("urlImagem");
                        String sobrenomeApi = jsonObject.getString("sobrenome");

                        if (urlFotoApi == null) {
                            urlFotoApi = "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png";
                        }
                        if(apelidoApi == null|| apelidoApi.isEmpty()){
                            nome.setText(nomeApi+" "+sobrenomeApi);
                        }else{
                            nome.setText(apelidoApi);
                        }

                        Glide.with(context).load(urlFotoApi).circleCrop().into(foto);

                        // Faça algo com os valores obtidos
                        Log.d("API_RESPONSE_GET_ID_USUARIO_PARCIAL", "Campos obtidos: nome: " + nomeApi + " urlFoto: " + urlFotoApi + " sobrenome: " + sobrenomeApi );

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_GET_ID_USUARIO_PARCIAL", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR_GET_ID_USUARIO_PARCIAL", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_GET_ID_USUARIO_PARCIAL", "Erro ao fazer a requisição: " + throwable.getMessage());
            }
        });
    }


    public void selecionarUsuarioPorEmail(String email, Context context, TextView nome, TextView biografia, ImageView foto, TextView erro) {
        erro.setTextColor(ContextCompat.getColor(context, R.color.azul_carregando));
        erro.setText("Carregando...");
        erro.setVisibility(View.VISIBLE);

        ApiService apiService = new ApiService(context);
        UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
        Call<ResponseBody> call = usuarioInterface.selecionarUsuarioPorEmail(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    erro.setVisibility(View.INVISIBLE);
                    erro.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    try {
                        String jsonResponse = response.body().string();

                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        String apelidoApi = jsonObject.getString("apelido");
                        String nomeApi = jsonObject.getString("nome");
                        String biografiaApi = jsonObject.getString("biografia");
                        String urlFotoApi = jsonObject.getString("urlImagem");
                        if (jsonObject.getString("urlImagem").equals("") || jsonObject.getString("urlImagem") == null) {
                            urlFotoApi = "https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png";
                        }

                        if (apelidoApi.equals("") || apelidoApi == null) {
                            nome.setText(nomeApi);
                        } else {
                            nome.setText(apelidoApi);
                        }

                        if (biografia.equals("") || biografia == null) {
                            biografia.setHint("Nada por aqui...");
                        } else {
                            biografia.setText(biografiaApi);
                        }

                        Glide.with(context).load(urlFotoApi).circleCrop().into(foto);
                        Log.d("API_RESPONSE_GET_EMAIL_USUARIO", "Campos obtidos: apelido: " + apelidoApi + " nome: " + nomeApi + " biografia: " + biografiaApi);

                    } catch (Exception e) {
                        Log.e("API_ERROR_GET_EMAIL_USUARIO", "Erro ao processar resposta: " + e.getMessage());
                        erro.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                        erro.setText("Falha ao obter dados do usuário");
                        erro.setVisibility(View.VISIBLE);
                    }
                } else {
                    erro.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    erro.setText("Falha ao obter dados do usuário");
                    erro.setVisibility(View.VISIBLE);
                    Log.e("API_ERROR_GET_EMAIL_USUARIO", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_GET_EMAIL_USUARIO", "Erro ao fazer a requisição: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro inesperado", "Erro ao obter dados do perfil\nMensagem: " + throwable.getMessage());
            }
        });
    }

    public String inserirUsuario(Usuario usuario, Context c, String[] id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);
        Call<ResponseBody> call = usuarioInterface.inserirUsuario(usuario);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String[] idUsuario = {""};
                if (response.isSuccessful()) {
                    try {
                        idUsuario[0] = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("API_RESPONSE_POST_USUARIO", "ID do usuário inserido via API: " + idUsuario[0]);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR_POST_USUARIO", "Erro ao inserir o usuário: " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_POST_USUARIO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
                id[0] = idUsuario[0];
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_POST_USUARIO", "Erro ao inserir o usuário: " + throwable.getMessage());
                aux.abrirDialogErro(c, "Erro ao cadastrar usuário", "Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
            }

        });
        return id[0];
    }
    public void atualizarUsuario(String id, Map<String, Object> campo, TextView erro, Context c) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);

        Call<ResponseBody> call = usuarioInterface.atualizarUsuario(id, campo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String[] resposta = {""};
                if (response.isSuccessful()) {
                    try {
                        resposta[0] = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (erro != null) {
                        erro.setTextColor(ContextCompat.getColor(c, R.color.azul_carregando));
                        erro.setText("Usuário atualizado com sucesso");
                        erro.setVisibility(View.VISIBLE);
                    }
                    Log.d("API_RESPONSE_PATCH_USUARIO", "Usuário atualizado via API: " + resposta[0]);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR_PATCH_USUARIO", "Erro ao atualizar o usuário: " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("API_ERROR_PATCH_USUARIO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_PATCH_USUARIO", "Erro ao atualizar o usuário: " + throwable.getMessage());
            }

        });
    }

    public void inserirUsuarioMongo(UsuarioMongo usuario, Context context) {;
        String urlApi2 = Geral.getInstance().getUrlApiMongo();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlApi2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);
        Call<ResponseBody> call = usuarioInterface.inserirUsuarioMongo(usuario);

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
                    Log.d("MONGO_API_RESPONSE_POST_USUARIO", "ID do usuário inserido via API mongo: " + body);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("MONGO_API_ERROR_POST_USUARIO", "Erro ao inserir o usuário mongo: " + response.code() + " - " + errorBody + " - " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MONGO_API_ERROR_POST_USUARIO", "Erro ao processar o corpo da resposta de erro.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("MONGO_API_ERROR_POST_USUARIO", "Erro ao inserir o usuário mongo: " + throwable.getMessage());
                aux.abrirDialogErro(context, "Erro ao cadastrar usuário mongo", "Não foi possível realizar seu cadastro. Erro: " + throwable.getMessage());
            }

        });
    }
}