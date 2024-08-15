package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TelaLogin extends AppCompatActivity {
    Button entrar;
    EditText email,senha;
    TextView errorEmail, errorSenha;

    String[] emailTeste = {"samira.campos@germinare.org.br","ana.romera@germinare.org.br"};
    String[] senhaTeste  = {"samira","12345678@"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        entrar = findViewById(R.id.btn_entrar);
        email = findViewById(R.id.email_login);
        senha = findViewById(R.id.senha_login);
        errorEmail = findViewById(R.id.erro_email);
        errorSenha = findViewById(R.id.erro_senha);


        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean erro = false;

                // Verificando se o e-mail é válido
                if (emailValido(email.getText().toString())) {
                    // E-mail é válido
                    if(email.getText().toString().equals(emailTeste[0]) || email.getText().toString().equals(emailTeste[1])){
                        semErroInput(errorEmail,email);
                        erro = false;
                    }else{
                        erroInput("Não há uma conta vinculada a esse e-mail",errorEmail,email);
                        erro = true;
                    }
                } else {
                    // E-mail é inválido
                    erroInput("E-mail inválido",errorEmail,email);
                    erro = true;
                }


                if((senha.getText().toString().equals(senhaTeste[0]) && email.getText().toString().equals(emailTeste[0]))|| (senha.getText().toString().equals(senhaTeste[1]) && email.getText().toString().equals(emailTeste[1]))){
                    semErroInput(errorSenha,senha);
                    erro = false;
                }else{
                    erroInput("Senha inválida",errorSenha,senha);
                    erro = true;
                }

                if(!erro){
                    Toast.makeText(TelaLogin.this, "Leva para o feed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Método para validar o e-mail
    public boolean emailValido(String emailString) {
        return emailString != null && Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }
    public void erroInput(String mensagem, TextView texto, EditText input){
        input.setBackground(ContextCompat.getDrawable(TelaLogin.this, R.drawable.input_erro));
        input.setHintTextColor(ContextCompat.getColor(TelaLogin.this, R.color.vermelho_erro_hint));
        //mostra mensagem de erro
        texto.setText(mensagem);
        texto.setVisibility(View.VISIBLE);
    }
    public void semErroInput(TextView erro, EditText input){
        input.setBackground(ContextCompat.getDrawable(TelaLogin.this, R.drawable.input));
        input.setHintTextColor(ContextCompat.getColor(TelaLogin.this, R.color.hint));
        erro.setVisibility(View.INVISIBLE);
    }

}