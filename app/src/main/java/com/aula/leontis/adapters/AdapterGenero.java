package com.aula.leontis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.utilities.MetodosAux;
import com.aula.leontis.R;
import com.aula.leontis.models.genero.Genero;

import java.util.List;

public class AdapterGenero extends RecyclerView.Adapter<AdapterGenero.viewHolderGenero>{
    MetodosAux aux = new MetodosAux();
    private List<Genero> listaGeneros;
    public AdapterGenero(List<Genero> listaGeneros){
        this.listaGeneros = listaGeneros;
    }
    @NonNull
    @Override
    public viewHolderGenero onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genero,parent,false);
        return new viewHolderGenero(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderGenero holder, int position) {
        holder.nomeGenero.setText(listaGeneros.get(holder.getAdapterPosition()).getNome());

        if (listaGeneros.get(holder.getAdapterPosition()).getCheckInteresse()){
            holder.checkInteresse.setChecked(true);
        }else{
            holder.checkInteresse.setChecked(false);
        }

        holder.checkInteresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkInteresse.isChecked()){
                    listaGeneros.get(holder.getAdapterPosition()).setCheckInteresse(true);
                }else{
                    listaGeneros.get(holder.getAdapterPosition()).setCheckInteresse(false);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String teste ="";
                teste += listaGeneros.get(holder.getAdapterPosition()).getNome() +" " + listaGeneros.get(holder.getAdapterPosition()).getCheckInteresse();
                Toast.makeText(holder.itemView.getContext(), teste, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aux.abrirDialog(holder.itemView.getContext(),
                        listaGeneros.get(holder.getAdapterPosition()).getNome(),
                        listaGeneros.get(holder.getAdapterPosition()).getIntroducao());
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaGeneros.size();
    }

    public class viewHolderGenero extends RecyclerView.ViewHolder{
        TextView nomeGenero;
        CheckBox checkInteresse;
        ImageButton btnInfo;


    public viewHolderGenero(@NonNull View itemView) {
        super(itemView);
        nomeGenero = itemView.findViewById(R.id.titulo_genero);
        checkInteresse = itemView.findViewById(R.id.check_interesse);
        btnInfo = itemView.findViewById(R.id.btn_info);

    }
}
}
