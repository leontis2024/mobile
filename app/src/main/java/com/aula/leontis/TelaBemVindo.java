package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TelaBemVindo extends AppCompatActivity {
    Button btnFinalizar;
    String nome, sobrenome, email, telefone, dtNasc, senha, apelido, biografia, sexo;
    long[] listaGenerosInteresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_bem_vindo);

        Intent info = getIntent();
        Bundle infoCadastro = info.getExtras();
        if(infoCadastro != null) {
            nome = infoCadastro.getString("nome");
            sobrenome = infoCadastro.getString("sobrenome");
            email = infoCadastro.getString("email");
            telefone = infoCadastro.getString("telefone");
            dtNasc = infoCadastro.getString("dtNasc");
            senha = infoCadastro.getString("senha");
            apelido = infoCadastro.getString("apelido");
            biografia = infoCadastro.getString("biografia");
            sexo = infoCadastro.getString("sexo");
            listaGenerosInteresse = infoCadastro.getLongArray("listaGenerosInteresse");
        }


        btnFinalizar = findViewById(R.id.btn_finalizar_bem_vindo);
        btnFinalizar.setOnClickListener(v -> {
            Intent feed = new Intent(TelaBemVindo.this, TelaPrincipal.class);
            startActivity(feed);
            finish();

        });
    }

}