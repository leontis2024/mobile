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
import com.aula.leontis.models.comentario.ComentarioResponse;
import com.aula.leontis.services.UsuarioService;
import com.aula.leontis.utilities.MetodosAux;

import java.util.List;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.viewHolderComentario> {
    UsuarioService usuarioService = new UsuarioService();
    MetodosAux aux = new MetodosAux();
    private List<ComentarioResponse> listaComentarioResponses;

    public AdapterComentario(List<ComentarioResponse> listaComentarioResponses) {
        this.listaComentarioResponses = listaComentarioResponses;
    }

    @NonNull
    @Override
    public viewHolderComentario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new viewHolderComentario(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderComentario holder, int position) {
        long idUsuario = listaComentarioResponses.get(holder.getAdapterPosition()).getUsuarioId();
        usuarioService.selecionarUsuarioPorIdParcial(String.valueOf(idUsuario), holder.itemView.getContext(),holder.imgComentario, holder.nomeUsuarioComentario);
        holder.comentario.setText(listaComentarioResponses.get(holder.getAdapterPosition()).getComentario().getTexto());

    }

    @Override
    public int getItemCount() {
        return listaComentarioResponses.size();
    }

    public class viewHolderComentario extends RecyclerView.ViewHolder {
        ImageView imgComentario;
        TextView comentario,nomeUsuarioComentario;


        public viewHolderComentario(@NonNull View itemView) {
            super(itemView);

            imgComentario = itemView.findViewById(R.id.imgComentario);
            nomeUsuarioComentario = itemView.findViewById(R.id.nomeUsuarioComentario);
            comentario = itemView.findViewById(R.id.comentario);

        }
    }

}