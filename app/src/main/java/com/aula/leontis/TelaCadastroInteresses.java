package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class TelaCadastroInteresses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_interesses);

        //pegando informações de cadastro das telas anteriores
        Intent infoCadastro = getIntent();
        String nome = infoCadastro.getStringExtra("nome");
        String sobrenome = infoCadastro.getStringExtra("sobrenome");
        String email = infoCadastro.getStringExtra("email");
        String telefone = infoCadastro.getStringExtra("telefone");
        String dtNasc = infoCadastro.getStringExtra("dtNasc");
        String senha = infoCadastro.getStringExtra("senha");
        String apelido = infoCadastro.getStringExtra("apelido");
        String biografia = infoCadastro.getStringExtra("biografia");
        String sexo = infoCadastro.getStringExtra("sexo");


    }
}