package com.aula.leontis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.leontis.R;
import com.aula.leontis.models.museu.Museu;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterMuseu extends RecyclerView.Adapter<AdapterMuseu.viewHolderMuseu>{
    private List<Museu> listaMuseus;

    public AdapterMuseu(List<Museu> listaMuseus) {
        this.listaMuseus = listaMuseus;
    }

    @NonNull
    @Override
    public viewHolderMuseu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_museu,parent,false);
        return new viewHolderMuseu(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderMuseu holder, int position) {
        holder.nomeMuseu.setText(listaMuseus.get(holder.getAdapterPosition()).getNomeMuseu());
        String url = listaMuseus.get(holder.getAdapterPosition()).getUrlImagem();
        if(url == null){
            url= "https://gamestation.com.br/wp-content/themes/game-station/images/image-not-found.png";
        }
        Glide.with(holder.imagemMuseu.getContext()).asBitmap().load(url).into(holder.imagemMuseu);

    }

    @Override
    public int getItemCount() {
        return listaMuseus.size();
    }

    public class viewHolderMuseu extends RecyclerView.ViewHolder {
        TextView nomeMuseu, enderecoMuseu, diaFuncionamentoMuseu, precoIngressoMuseu;
        ImageView imagemMuseu;

        public viewHolderMuseu(@NonNull View itemView) {
            super(itemView);
            nomeMuseu = itemView.findViewById(R.id.nomeMuseu);
            enderecoMuseu = itemView.findViewById(R.id.enderecoMuseu);
            diaFuncionamentoMuseu = itemView.findViewById(R.id.diaFuncionamentoMuseu);
            precoIngressoMuseu = itemView.findViewById(R.id.precoIngressoMuseu);
            imagemMuseu = itemView.findViewById(R.id.imgMuseu);

        }
    }
}
