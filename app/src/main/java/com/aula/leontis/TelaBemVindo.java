package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TelaBemVindo extends AppCompatActivity {
    Button btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_bem_vindo);

        btnFinalizar = findViewById(R.id.btn_finalizar_bem_vindo);
        btnFinalizar.setOnClickListener(v -> {
            Toast.makeText(this, "deu bom", Toast.LENGTH_SHORT).show();
            logout();
        });
    }
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(TelaBemVindo.this, TelaLogin.class);
        startActivity(login);
        finish();
    }
}