package com.aula.leontis.adapters;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaScanner;
import com.aula.leontis.interfaces.usuario.UsuarioInterface;
import com.aula.leontis.models.guia.ObraGuia;
import com.aula.leontis.services.ApiService;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.utilities.MetodosAux;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterObraGuia extends RecyclerView.Adapter<AdapterObraGuia.viewHolderObraGuia>{
        MetodosAux aux = new MetodosAux();
        ObraService obraService = new ObraService();
        private List<ObraGuia> listaObraGuias;
        String idUsuario;
        public AdapterObraGuia(List<ObraGuia> listaObraGuias){
            this.listaObraGuias = listaObraGuias;
        }
        @NonNull
        @Override
        public com.aula.leontis.adapters.AdapterObraGuia.viewHolderObraGuia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guia_mapa,parent,false);
            return new com.aula.leontis.adapters.AdapterObraGuia.viewHolderObraGuia(viewItem);
        }

        @Override
        public void onBindViewHolder(@NonNull com.aula.leontis.adapters.AdapterObraGuia.viewHolderObraGuia holder, int position) {
            holder.numeroOrdem.setText(listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem()+"");
            holder.item.setImageResource(R.drawable.item_mapa);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.item.setImageResource(R.drawable.item_mapa_apertado);
                    Dialog dialog = new Dialog(holder.itemView.getContext());
                    dialog.setContentView(R.layout.dialog_obra_guia);
                    dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.caixa_mensagem_fundo);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            // Ação ao fechar o dialog
                            holder.item.setImageResource(R.drawable.item_mapa); // Exemplo: redefinir a imagem
                        }
                    });

                    TextView localizaco = dialog.findViewById(R.id.descLocalizacao);
                    ImageView obra = dialog.findViewById(R.id.imagemObra);
                    obraService.buscarObraPorIdParcial(listaObraGuias.get(holder.getAdapterPosition()).getIdObra().toString(), holder.itemView.getContext(), obra, localizaco, listaObraGuias.get(holder.getAdapterPosition()).getDescLocalizacao());

                    Button acessarScanner = dialog.findViewById(R.id.btnAcessarScanner);
                    acessarScanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(holder.itemView.getContext(), TelaScanner.class);
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String email = auth.getCurrentUser().getEmail();
                            selecionarIdUsuarioPorEmail(email,holder,bundle,intent);

                        }
                    });
                    dialog.show();

                }
            });


//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
//            params.setMargins(listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem()*10, listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem()*10, 0, 0);
//            holder.itemView.setLayoutParams(params);


        }

        @Override
        public int getItemCount() {
            return listaObraGuias.size();
        }

        public class viewHolderObraGuia extends RecyclerView.ViewHolder{
            TextView numeroOrdem;

            ImageView item;

            public viewHolderObraGuia(@NonNull View itemView) {
                super(itemView);
                numeroOrdem = itemView.findViewById(R.id.numOrdem);
                item = itemView.findViewById(R.id.item);


            }
        }
    public void selecionarIdUsuarioPorEmail(String email,AdapterObraGuia.viewHolderObraGuia holder,Bundle bundle, Intent intent) {

            ApiService apiService = new ApiService(holder.itemView.getContext());
            UsuarioInterface usuarioInterface = apiService.getUsuarioInterface();
            Call<ResponseBody> call = usuarioInterface.selecionarUsuarioPorEmail(email);

            //executar chamada
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            // Converte o corpo da resposta para string
                            String jsonResponse = response.body().string();

                            // Cria um JSONObject a partir da string
                            JSONObject jsonObject = new JSONObject(jsonResponse);

                            String idApi = jsonObject.getString("id");
                            idUsuario = idApi;
                            bundle.putString("id", idUsuario);
                            bundle.putString("idGuia", listaObraGuias.get(holder.getAdapterPosition()).getIdGuia().toString());
                            bundle.putInt("nrOrdem", listaObraGuias.get(holder.getAdapterPosition()).getNrOrdem());
                            intent.putExtras(bundle);
                            holder.itemView.getContext().startActivity(intent);

                            // Faça algo com os valores obtidos
                            Log.d("API_RESPONSE_GET_EMAIL", "Campo obtido: id: " + idApi);

                        } catch (Exception e) {
                            Log.e("API_RESPONSE_GET_EMAIL", "Erro ao processar resposta: " + e.getMessage());

                        }
                    } else {
                        Log.e("API_ERROR_GET_EMAIL", "Erro na resposta da API: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("API_ERROR_GET_EMAIL", "Erro ao fazer a requisição: " + throwable.getMessage());

                }
            });
        }
    }


