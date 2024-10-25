package com.aula.leontis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.models.genero.Genero;
import com.aula.leontis.utilities.MetodosAux;

import java.util.List;

public class AdapterGeneroFiltro extends RecyclerView.Adapter<com.aula.leontis.adapters.AdapterGeneroFiltro.viewHolderGeneroFiltro>{

        MetodosAux aux = new MetodosAux();
        private List<Genero> listaGeneros;
        public AdapterGeneroFiltro(List < Genero > listaGeneros) {
        this.listaGeneros = listaGeneros;
    }
        @NonNull
        @Override
        public com.aula.leontis.adapters.AdapterGeneroFiltro.viewHolderGeneroFiltro onCreateViewHolder
        (@NonNull ViewGroup parent,int viewType){
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genero2, parent, false);
        return new com.aula.leontis.adapters.AdapterGeneroFiltro.viewHolderGeneroFiltro(viewItem);
    }

        @Override
        public void onBindViewHolder
        (@NonNull com.aula.leontis.adapters.AdapterGeneroFiltro.viewHolderGeneroFiltro holder,
        int position){
        holder.nomeGenero.setText(listaGeneros.get(holder.getAdapterPosition()).getNome());

        if (listaGeneros.get(holder.getAdapterPosition()).getCheckInteresse()) {
            holder.checkInteresse.setChecked(true);
        } else {
            holder.checkInteresse.setChecked(false);
        }

        holder.checkInteresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkInteresse.isChecked()) {
                    listaGeneros.get(holder.getAdapterPosition()).setCheckInteresse(true);
                } else {
                    listaGeneros.get(holder.getAdapterPosition()).setCheckInteresse(false);
                }
            }
        });


    }

        @Override
        public int getItemCount () {
        return listaGeneros.size();
    }

        public class viewHolderGeneroFiltro extends RecyclerView.ViewHolder {
            TextView nomeGenero;
            CheckBox checkInteresse;
            ImageButton btnInfo;


            public viewHolderGeneroFiltro(@NonNull View itemView) {
                super(itemView);
                nomeGenero = itemView.findViewById(R.id.titulo_genero);
                checkInteresse = itemView.findViewById(R.id.check_interesse);
                btnInfo = itemView.findViewById(R.id.btn_info);

            }
        }
    }
    


