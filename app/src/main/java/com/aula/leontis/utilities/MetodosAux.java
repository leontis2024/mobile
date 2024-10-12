package com.aula.leontis.utilities;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaLogin;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.services.ApiService;
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
import java.util.Date;
import java.util.TimeZone;
public class MetodosAux {

    public void abrirDialog(Context c, String texto, String mensagem){
        Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.caixa_mensagem);
        dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        TextView titulo = dialog.findViewById(R.id.titulo);
        TextView introducao = dialog.findViewById(R.id.mensagem);
        titulo.setText(texto);
        introducao.setText(mensagem);

        Button btnFechar = dialog.findViewById(R.id.btn_fechar);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void abrirDialogErro(Context c, String texto, String mensagem){
        Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.caixa_mensagem);
        dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        TextView titulo = dialog.findViewById(R.id.titulo);
        titulo.setTextColor(c.getResources().getColor(R.color.vinho_escuro));
        titulo.setTypeface(null, Typeface.BOLD);
        TextView introducao = dialog.findViewById(R.id.mensagem);
        titulo.setText(texto);
        introducao.setText(mensagem);

        Button btnFechar = dialog.findViewById(R.id.btn_fechar);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void abrirDialogConfirmacao(Context c,String texto, String mensagem, boolean deletar,String id){
        Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.aviso_confirmacao);
        dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
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
                if(deletar) {
                    abrirDialog(dialog.getContext(), "Tchau tchau 😭...", "É triste que você queira ir embora, espero que nos encontremos algum outro dia 🥺");
                    FirebaseAuth.getInstance().signOut();
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    //  Referenciar o arquivo com o caminho completo (pode variar dependendo da sua organização de pastas)
                    StorageReference storageRef = storage.getReference().child("usuarios/usuario" + id+".jpg");

                    // Deletar o arquivo
                    if(storageRef!=null) {
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
                    }
                    deletarUsuarioPorIdMongo(id, c);
                    deletarUsuarioPorId(id, c);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(c, TelaLogin.class);
                            c.startActivity(intent);
                            ((Activity) c).finish();
                        }
                    },3000);
                }
            }
        });
        dialog.show();
    }
    public void deletarUsuarioPorId(String id, Context context) {
//        String urlAPI = "https://dev2-tfqz.onrender.com/";
//
//        // Configurar acesso à API
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(urlAPI)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        UsuarioInterface usuarioInterface = retrofit.create(UsuarioInterface.class);
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
                abrirDialogErro(context,"Erro ao deletar usário","Houve um erro ao deletar o usuário\nMensagem: "+throwable.getMessage());
            }
        });
    }

    public void deletarUsuarioPorIdMongo(String id, Context context) {
                String urlAPI = "https://apimongo-r613.onrender.com/";

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
                    Log.e("API_RESPONSE_DELETE", "Erro na resposta da API mongo: " + response.code()+" "+response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_ERROR_DELETE", "Erro ao fazer a requisição mongo: " + throwable.getMessage());
                abrirDialogErro(context,"Erro ao deletar usário","Houve um erro ao deletar o usuário mongo\nMensagem: "+throwable.getMessage());
            }
        });
    }


        public  String dataAtualFormatada() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.format(new Date());
        }


}
