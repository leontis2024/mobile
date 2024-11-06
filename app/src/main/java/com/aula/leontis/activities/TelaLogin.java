package com.aula.leontis.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aula.leontis.Geral;
import com.aula.leontis.R;
import com.aula.leontis.TokenManager;
import com.aula.leontis.interfaces.AuthInterface;
import com.aula.leontis.models.auth.LoginRequest;
import com.aula.leontis.services.RedisService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaLogin extends AppCompatActivity {
    Button entrar;
    EditText email,senha;
    RedisService redisService = new RedisService();
    Bundle novo = new Bundle();
    TextView errorEmail, errorSenha, cadastro, erroGeral;
    ImageButton btnOlho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);
        Bundle bundle = getIntent().getExtras();
        boolean cadastroUsuario = false;
        boolean tokenExpirado = false;
        boolean logout = false;
        boolean deletado = false;
        String emailCadastro = "";

        if(bundle != null) {
            cadastroUsuario = bundle.getBoolean("cadastro",false);
            tokenExpirado = bundle.getBoolean("tokenExpirado",false);
            logout = bundle.getBoolean("logout",false);
            deletado = bundle.getBoolean("deletado",false);
            if(tokenExpirado==true || logout==true || deletado==true) {
                redisService.decrementarAtividadeUsuario();
            }

        }
        entrar = findViewById(R.id.btn_entrar);
        email = findViewById(R.id.email_login);
        senha = findViewById(R.id.senha_login);
        errorEmail = findViewById(R.id.erro_email);
        errorSenha = findViewById(R.id.erro_senha);
        cadastro = findViewById(R.id.cadastrar);
        erroGeral = findViewById(R.id.erro_geral);
        btnOlho= findViewById(R.id.verSenha);
        if(!cadastroUsuario) {
            email.setText("");
            verificarUsuarioLogado();
        }else{
            emailCadastro = bundle.getString("email");
            email.setText(emailCadastro);
        }
        btnOlho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnOlho.getContentDescription().equals("fechado")) {

                    if (senha != null && !(senha.getText().equals(""))) {
                        btnOlho.setContentDescription("aberto");
                        btnOlho.setImageResource(R.drawable.olhinho);
                        senha.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }else{

                    if (senha != null && !(senha.getText().equals(""))) {
                        btnOlho.setContentDescription("fechado");
                        btnOlho.setImageResource(R.drawable.olhinho_fechado);
                        senha.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
            }
        });


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
                            pegarToken();
                            redisService.incrementarAtividadeUsuario();
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
            feed.putExtras(novo);
            startActivity(feed);
            finish();
        }
    }
    public void pegarToken() {
        // Instancia ApiService para lidar com a autenticação
        String urlApi = Geral.getInstance().getUrlApiSql();
        Retrofit authRetrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AuthInterface authInterface = authRetrofit.create(AuthInterface.class);// Obtém a interface de autenticação

        // Cria o objeto LoginRequest com as credenciais do usuário
        LoginRequest loginRequest = new LoginRequest(email.getText().toString(), senha.getText().toString());

        // Faz a requisição para a API de login
        authInterface.login(loginRequest).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                // Verifica se a resposta foi bem-sucedida e contém o token
                if (response.isSuccessful() && response.body() != null) {
                    // Pega o token de acesso e o token de refresh da resposta
                    Map<String, String> tokens =  response.body();

                    // Obtendo os tokens do Map
                    String accessToken = tokens.get("accessToken");
                    String refreshToken = tokens.get("refreshToken");

                    // Salva os tokens no TokenManager
                    TokenManager tokenManager = new TokenManager(TelaLogin.this);
                    tokenManager.saveAccessToken(accessToken);
                    tokenManager.saveRefreshToken(refreshToken);

                    Log.d("TOKEN", "Access Token: " + accessToken);
                    Log.d("TOKEN", "Refresh Token: " + refreshToken);

                    Intent main = new Intent(TelaLogin.this, TelaPrincipal.class);
                    startActivity(main);
                    finish();

                } else {
                    // Tratar erro de login (credenciais incorretas, por exemplo)
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                // Lida com erros de conexão ou outros tipos de falha
                Log.e("ERRO_LOGIN", "Erro: " + t.getMessage());
            }
        });
    }



}