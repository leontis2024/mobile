package com.aula.leontis.services;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaScanner;
import com.aula.leontis.interfaces.ModeloScannerInterface;
import com.aula.leontis.interfaces.mongo.MongoInterface;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.modelo.ModeloRequest;
import com.aula.leontis.models.modelo.ModeloResponse;
import com.bumptech.glide.Glide;



import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModeloService {
    ObraService obraService = new ObraService();
    public void preditarObra(String url, Context context, String idUsuario,TextView idObra,String idGuia,int nrOrdem,TextView erro,ImageView borda) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Geral.getInstance().getUrlModeloScanner())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ModeloScannerInterface modeloScannerInterface = retrofit.create(ModeloScannerInterface.class);

        Call<ModeloResponse> call = modeloScannerInterface.preditarObra(new ModeloRequest(url));

        call.enqueue(new Callback<ModeloResponse>() {
            @Override
            public void onResponse(Call<ModeloResponse> call, Response<ModeloResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        erro.setVisibility(View.INVISIBLE);
                        erro.setTextColor(ContextCompat.getColor(context, R.color.bege_claro));
                        borda.setImageResource(R.drawable.borda_escaner);
                        ModeloResponse modeloResponse = response.body();
                        if(Integer.parseInt(modeloResponse.getObra_predita())>0) {
                            obraService.buscarObraPorIdScanner(idUsuario, modeloResponse.getObra_predita(), idObra, idGuia, nrOrdem, context, erro, borda);
                        }else{
                            borda.setImageResource(R.drawable.borda_scan_erro);
                            erro.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                            erro.setText("Não foi possível escanear esta obra");
                            erro.setVisibility(View.VISIBLE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    erro.setVisibility(View.INVISIBLE);
                                    borda.setImageResource(R.drawable.borda_escaner);
                                }
                            }, 3000);
                        }
                        Log.d("OBRA_PREDITA",modeloResponse.getObra_predita());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("OBRA_PREDITA_ERROR", e.getMessage());
                    }
                } else {
                    borda.setImageResource(R.drawable.borda_scan_erro);
                    erro.setTextColor(ContextCompat.getColor(context, R.color.vermelho_erro));
                    erro.setText("Não foi possível escanear esta obra");
                    erro.setVisibility(View.VISIBLE);
                    Log.e("OBRA_PREDITA_ERROR", ""+response.code());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            erro.setVisibility(View.INVISIBLE);
                            borda.setImageResource(R.drawable.borda_escaner);
                        }
                    }, 3000);
                }
            }

            @Override
            public void onFailure(Call<ModeloResponse> call, Throwable throwable) {
                Log.e("OBRA_PREDITA_ERROR", throwable.getMessage());
            }
        });
    }
}
