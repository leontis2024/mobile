package com.aula.leontis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class TelaLogin extends AppCompatActivity {
    Button entrar;
    EditText email,senha;
    TextView errorEmail, errorSenha, cadastro, erroGeral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        verificarUsuarioLogado();

        entrar = findViewById(R.id.btn_entrar);
        email = findViewById(R.id.email_login);
        senha = findViewById(R.id.senha_login);
        errorEmail = findViewById(R.id.erro_email);
        errorSenha = findViewById(R.id.erro_senha);
        cadastro = findViewById(R.id.cadastrar);
        erroGeral = findViewById(R.id.erro_geral);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrindo a tela de cadastro
                Intent telCadastro = new Intent(TelaLogin.this, TelaCadastro.class);
                startActivity(telCadastro);
            }
        });


        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erroEmail = false;
                boolean erroSenha= false;

                // Verificando se o e-mail é válido
                if(email.getText().toString()==null || email.getText().toString().equals("")){
                    erroInput("Digite seu e-mail",errorEmail,email);
                    erroEmail = true;

                }else{
                    if (emailValido(email.getText().toString())) {
                        semErroInput(errorEmail,email);
                        erroEmail = false;
                    } else {
                        // E-mail é inválido, erro
                        erroInput("E-mail inválido",errorEmail,email);
                        erroEmail = true;
                    }
                }


                //verificando se o e-mail e a senha estão entre os de teste
                if(senha.getText().toString()==null || senha.getText().toString().equals("")){
                    erroInput("Digite sua senha",errorSenha,senha);
                    erroSenha = true;
                }else if(senha.getText().length() < 6 || senha.getText().length() > 20) {
                    erroInput("Senha inválida",errorSenha,senha);
                    erroSenha = true;
                }
                else{
                    semErroInput(errorSenha,senha);
                    erroSenha = false;
                }
                //leva para a tela do feed
                if(!erroEmail && !erroSenha){
                    fazerAutenticacao(email.getText().toString(), senha.getText().toString());
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
    public void fazerAutenticacao(String emailStr, String senhaStr){
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        autenticar.signInWithEmailAndPassword(emailStr, senhaStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String msg="Não foi possível efetuar sua autenticação";
                        if (task.isSuccessful()) {
                            //redirecionar para a proxima tela
                            erroGeral.setVisibility(View.INVISIBLE);
                            Intent main = new Intent(TelaLogin.this, TelaPrincipal.class);
                            startActivity(main);
                            finish();
                        }else{
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                msg="Email ou Senha inválidos";
                            }catch (Exception e){
                                Log.e("ERRO",e.getMessage());

                            }
                            erroGeral.setText(msg);
                            erroGeral.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    public void verificarUsuarioLogado(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            Intent feed = new Intent(TelaLogin.this, TelaPrincipal.class);
            startActivity(feed);
            finish();
        }
    }

}