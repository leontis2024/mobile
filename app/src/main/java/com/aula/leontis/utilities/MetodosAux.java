package com.aula.leontis.utilities;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aula.leontis.Geral;

import com.aula.leontis.NotificationReceiver;
import com.aula.leontis.R;
import com.aula.leontis.activities.TelaLogin;
import com.aula.leontis.activities.TelaPesquisa;
import com.aula.leontis.interfaces.mongo.MongoInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.avaliacao.Avaliacao;
import com.aula.leontis.models.comentario.Comentario;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.RedisService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
public class MetodosAux {
    RedisService redisService = new RedisService();

    public void abrirDialog(Context c, String texto, String mensagem) {
        Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.caixa_mensagem);
        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        TextView titulo = dialog.findViewById(R.id.titulo);
        TextView introducao = dialog.findViewById(R.id.descLocalizacao);
        titulo.setText(texto);
        introducao.setText(mensagem);

        Button btnFechar = dialog.findViewById(R.id.btnAcessarScanner);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void abrirDialogErro(Context c, String texto, String mensagem) {
        Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.caixa_mensagem);
        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        TextView titulo = dialog.findViewById(R.id.titulo);
        titulo.setTextColor(c.getResources().getColor(R.color.vinho_escuro));
        titulo.setTypeface(null, Typeface.BOLD);
        TextView introducao = dialog.findViewById(R.id.descLocalizacao);
        titulo.setText(texto);
        introducao.setText(mensagem);

        Button btnFechar = dialog.findViewById(R.id.btnAcessarScanner);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void abrirDialogConfirmacao(Context c, String texto, String mensagem, boolean deletar, String id) {
        Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.aviso_confirmacao);
        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        TextView titulo = dialog.findViewById(R.id.titulo_aviso);
        titulo.setTextColor(c.getResources().getColor(R.color.vinho_escuro));
        titulo.setTypeface(null, Typeface.BOLD);
        TextView introducao = dialog.findViewById(R.id.mensagem_aviso);
        titulo.setText(texto);
        introducao.setText(mensagem);

        Button btnCancelar = dialog.findViewById(R.id.btn_cancelar);
        Button btnConfirmar = dialog.findViewById(R.id.btn_deletar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (deletar) {
                    abrirDialog(dialog.getContext(), "Tchau tchau 😭...", "É triste que você queira ir embora, espero que nos encontremos algum outro dia 🥺");
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                    FirebaseAuth.getInstance().signOut();
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    //  Referenciar o arquivo com o caminho completo (pode variar dependendo da sua organização de pastas)
                    StorageReference storageRef = storage.getReference().child("usuarios/usuario" + id + ".jpg");

                    // Deletar o arquivo
                    if (storageRef != null) {
                        try {
                            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Imagem deletada com sucesso
                                    Log.d("IMAGEM_DELETE", "Imagem deletada com sucesso.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Falha ao deletar a imagem
                                    Log.e("IMAGEM_DELETE_ERROR", "Erro ao deletar a imagem: " + exception.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            Log.e("IMAGEM_DELETE_ERROR", "Erro ao deletar a imagem: " + e.getMessage());
                        }
                    }
                    List<Comentario> comentarios = new ArrayList<>();
                    List<Avaliacao> avaliacaos = new ArrayList<>();
                    buscarComentariosPorIdUsuario(id, c, comentarios);
                    buscarAvaliacaoPorIdUsuario(id, c, avaliacaos);
                    Handler esperar = new Handler();
                    esperar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (Comentario c : comentarios) {
                                redisService.decrementarComentarioObra(c.getObraId() + "");
                            }
                            for (Avaliacao a : avaliacaos) {
                                redisService.decrementarAvaliacaoObra(a.getObraId() + "");
                            }
                            deletarUsuarioPorIdMongo(id, c);
                            deletarUsuarioPorId(id, c);
                        }
                    }, 3000);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(c, TelaLogin.class);
                            bundle.putBoolean("deletado",true);
                            intent.putExtras(bundle);
                            c.startActivity(intent);
                            ((Activity) c).finish();
                        }
                    }, 6000);
                }
            }
        });
        dialog.show();
    }

    public void deletarUsuarioPorId(String id, Context context) {

        ApiService apiService = new ApiService(context);
        UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
        Call<ResponseBody> call = usuarioInterface.deletarUsuario(id);

        //executar chamada
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Log.d("API_RESPONSE_DELETE", "Usuário deletado: " + response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Log.e("API_RESPONSE_DELETE", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_DELETE", "Erro ao fazer a requisição: " + throwable.getMessage());
                abrirDialogErro(context, "Erro ao deletar usário", "Houve um erro ao deletar o usuário\nMensagem: " + throwable.getMessage());
            }
        });
    }

    public void deletarUsuarioPorIdMongo(String id, Context context) {
        String urlAPI = Geral.getInstance().getUrlApiMongo();

        // Configurar acesso à API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);
        Call<ResponseBody> call = usuarioInterface.deletarUsuarioMongo(id);

        //executar chamada
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Log.d("API_RESPONSE_DELETE", "Usuário deletado no mongo: " + response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Log.e("API_RESPONSE_DELETE", "Erro na resposta da API mongo: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_DELETE", "Erro ao fazer a requisição mongo: " + throwable.getMessage());
            }
        });
    }


    public String dataAtualFormatada() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    public void abrirDialogPrimeiroAcesso(Context c, String texto, String mensagem) {
        Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.caixa_mensagem2);
        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        TextView titulo = dialog.findViewById(R.id.titulo);
        TextView introducao = dialog.findViewById(R.id.descLocalizacao);
        titulo.setText(texto);
        introducao.setText(mensagem);

        Button btnFechar = dialog.findViewById(R.id.btnAcessarScanner);
        btnFechar.setText("Responder Pesquisa");
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, TelaPesquisa.class);
                Geral.getInstance().setPrimeiroAcesso(false);
                c.startActivity(intent);
            }
        });
        dialog.show();
    }

    public void buscarComentariosPorIdUsuario(String usuarioId, Context context, List<Comentario> listaComentarios) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Geral.getInstance().getUrlApiMongo())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<List<Comentario>> call = mongoInterface.buscarComentariosPorId(Long.parseLong(usuarioId));

        call.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaComentarios.clear();
                    listaComentarios.addAll(response.body());


                }
            }

            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {
                Log.e("MONGO_API_ERROR_GET_COMENTARIOS", "Erro ao fazer a requisição: " + t.getMessage());
                abrirDialogErro(context, "Erro inesperado", "Erro ao obter comentarios\nMensagem: " + t.getMessage());
            }
        });
    }

    public void buscarAvaliacaoPorIdUsuario(String usuarioId, Context context, List<Avaliacao> listaAvaliacoes) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Geral.getInstance().getUrlApiMongo())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MongoInterface mongoInterface = retrofit.create(MongoInterface.class);
        Call<List<Avaliacao>> call = mongoInterface.buscarAvaliacoesPorId(Long.parseLong(usuarioId));

        call.enqueue(new Callback<List<Avaliacao>>() {
            @Override
            public void onResponse(Call<List<Avaliacao>> call, Response<List<Avaliacao>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaAvaliacoes.clear();
                    listaAvaliacoes.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<Avaliacao>> call, Throwable t) {
                Log.e("MONGO_API_ERROR_GET_COMENTARIOS", "Erro ao fazer a requisição: " + t.getMessage());
                abrirDialogErro(context, "Erro inesperado", "Erro ao obter comentarios\nMensagem: " + t.getMessage());
            }
        });
    }

    public void agendarNotificacaoDiaria(Context context) {
        // Defina a hora para exibir a notificação
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Se a hora definida já passou no dia atual, agendar para o próximo dia
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Intent para o BroadcastReceiver
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Agendar o alarme
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Repetir o alarme uma vez por dia
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void agendarNotificacaoHoraEmHora(Context context) {
        // Defina o horário inicial para o próximo início de hora
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Se a hora já passou, configurar para o próximo início de hora
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        // Intent para o BroadcastReceiver
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Agendar o alarme
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Agendar alarme exato para o próximo início de hora
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

}
