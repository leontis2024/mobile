package com.aula.leontis.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aula.leontis.R;
import com.aula.leontis.services.UsuarioService;

import java.util.HashMap;
import java.util.Map;

public class TelaEditarPerfil extends AppCompatActivity {
    EditText apelido,biografia;
    ImageView fotoPerfil;
    ImageButton btnVoltar,btnMaisCampos,btnFinalizar;
    UsuarioService usuarioService = new UsuarioService();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_editar_perfil);
        apelido = findViewById(R.id.apelido);
        biografia = findViewById(R.id.biografia);
        fotoPerfil = findViewById(R.id.fotoPerfil);

        btnVoltar = findViewById(R.id.btnVoltarEdit);
        btnMaisCampos = findViewById(R.id.btnEditarMaisCampos);
        btnFinalizar = findViewById(R.id.btnFinalizarEdicao);

        Bundle info = getIntent().getExtras();
        if(info!=null) {
            id = info.getString("id");
            usuarioService.selecionarUsuarioPorId(id,this,apelido,biografia,fotoPerfil);
        }
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> update = new HashMap<>();
                update.put("apelido",apelido.getText().toString());
                update.put("biografia",biografia.getText().toString());
                usuarioService.atualizarUsuario(id,update);
            }
        });


    }
}