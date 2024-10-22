package com.aula.leontis.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.activitys.TelaInfoObra;
import com.aula.leontis.models.comentario.ComentarioResponse;
import com.aula.leontis.models.historico.Historico;
import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.services.UsuarioService;
import com.aula.leontis.utilities.MetodosAux;

import java.util.List;

public class AdapterHistorico extends RecyclerView.Adapter<com.aula.leontis.adapters.AdapterHistorico.viewHolderHistorico>{
        ObraService obraService = new ObraService();
        MetodosAux aux = new MetodosAux();
        private List<Historico> listaHistorico;

        public AdapterHistorico(List<Historico> listaHistorico) {
            this.listaHistorico = listaHistorico;
        }

        @NonNull
        @Override
        public AdapterHistorico.viewHolderHistorico onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_obra, parent, false);
            return new AdapterHistorico.viewHolderHistorico(viewItem);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterHistorico.viewHolderHistorico holder, int position) {
            obraService.buscarObraPorIdParcial(listaHistorico.get(holder.getAdapterPosition()).getObraId().toString(), holder.itemView.getContext(),holder.imgObra,null,"");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",listaHistorico.get(holder.getAdapterPosition()).getObraId().toString());
                    Intent intent = new Intent(holder.itemView.getContext(), TelaInfoObra.class);
                    intent.putExtras(bundle);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaHistorico.size();
        }

        public class viewHolderHistorico extends RecyclerView.ViewHolder {
            ImageView imgObra;


            public viewHolderHistorico(@NonNull View itemView) {
                super(itemView);

                imgObra = itemView.findViewById(R.id.imgObra);


            }
        }


}
