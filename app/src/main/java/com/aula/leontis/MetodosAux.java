package com.aula.leontis;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
}
