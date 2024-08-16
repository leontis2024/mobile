package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TelaCadastro extends AppCompatActivity {
    ImageButton btCalendar;
    EditText nome,sobrenome,email,telefone,dtNasc,senha;
    Button continuar;
    TextView erroNome,erroSobrenome,erroEmail,erroTelefone,erroDtNasc,erroSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        nome = findViewById(R.id.nome_cadastro);
        erroNome = findViewById(R.id.erro_nome_cadastro);

        sobrenome = findViewById(R.id.sobrenome_cadastro);
        erroSobrenome = findViewById(R.id.erro_sobrenome_cadastro);

        email = findViewById(R.id.email_cadastro);
        erroEmail = findViewById(R.id.erro_email_cadastro);

        telefone = findViewById(R.id.telefone_cadastro);
        erroTelefone = findViewById(R.id.erro_telefone_cadastro);

        dtNasc = findViewById(R.id.data_nasc_cadastro);
        erroDtNasc = findViewById(R.id.erro_nasc_cadastro);

        senha = findViewById(R.id.senha_cadastro);
        erroSenha = findViewById(R.id.erro_senha_cadastro);

        continuar = findViewById(R.id.btn_continuar_cadastro);
        btCalendar = findViewById(R.id.calendario_cadastro);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean erro = false;
                //verificando se os input estão vazios, se sim da erro
                if(nome.getText().toString()==null || nome.getText().toString().equals("")){
                    erroInput("Digite seu nome",erroNome,nome);
                    erro = true;
                }else{
                    semErroInput(erroNome,nome);
                    erro = false;
                }
                if(sobrenome.getText().toString()==null || sobrenome.getText().toString().equals("")){
                    erroInput("Digite seu sobrenome",erroSobrenome,sobrenome);
                    erro = true;
                }else{
                    semErroInput(erroSobrenome,sobrenome);
                    erro = false;
                }
                if(email.getText().toString()==null || email.getText().toString().equals("")) {
                    erroInput("Digite seu e-mail",erroEmail,email);
                    erro = true;
                }else if(!emailValido(email.getText().toString())) {
                    //verificando se o e-mail é válido
                    erroInput("Digite um e-mail válido", erroEmail, email);
                    erro = true;
                }else{
                    semErroInput(erroEmail,email);
                    erro = false;
                }
                if(telefone.getText().toString()==null || telefone.getText().toString().equals("")){
                    erroInput("Digite seu telefone",erroTelefone,telefone);
                    erro = true;
                }else{
                    semErroInput(erroTelefone,telefone);
                    erro = false;
                }
                if(dtNasc.getText().toString()==null || dtNasc.getText().toString().equals("")){
                    erroInput("Digite sua data de nascimento",erroDtNasc,dtNasc);
                    erro = true;
                }else{
                    semErroInput(erroDtNasc,dtNasc);
                    erro = false;
                }
                if(senha.getText().toString()==null || senha.getText().toString().equals("")){
                    erroInput("Digite sua senha",erroSenha,senha);
                }else{
                    semErroInput(erroSenha,senha);
                    erro = false;
                }
                if(!erro){
                    //caso não de erro, cria um bundle com as informações de login e passa para a tela de cadastro2
                    Bundle infoCadastro = new Bundle();
                    infoCadastro.putString("nome",nome.getText().toString());
                    infoCadastro.putString("sobrenome",sobrenome.getText().toString());
                    infoCadastro.putString("email",email.getText().toString());
                    infoCadastro.putString("dtNasc",dtNasc.getText().toString());
                    infoCadastro.putString("tel",telefone.getText().toString());
                    infoCadastro.putString("senha",senha.getText().toString());

                    Intent telaCadastro2 = new Intent(TelaCadastro.this, TelaCadastro2.class);
                    telaCadastro2.putExtras(infoCadastro);
                    startActivity(telaCadastro2);
                    finish();
                }

            }
        });

        //configurando o calendário
        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TelaCadastro.this, null, year, month, dayOfMonth);

                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Handle the selected date
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        dtNasc.setText(selectedDate);
                    }
                });

                dialog.show();
            }
        });
    }
    //verifica se o e-mail é válido
    public boolean emailValido(String emailString) {
        return emailString != null && Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }
    //mostra mensagem de erro
    public void erroInput(String mensagem, TextView texto, EditText input){
        input.setBackground(ContextCompat.getDrawable(TelaCadastro.this, R.drawable.input_erro));
        input.setHintTextColor(ContextCompat.getColor(TelaCadastro.this, R.color.vermelho_erro_hint));
        //mostra mensagem de erro
        texto.setText(mensagem);
        texto.setVisibility(View.VISIBLE);
    }
    //oculta mensagem de erro
    public void semErroInput(TextView erro, EditText input){
        input.setBackground(ContextCompat.getDrawable(TelaCadastro.this, R.drawable.input));
        input.setHintTextColor(ContextCompat.getColor(TelaCadastro.this, R.color.hint));
        erro.setVisibility(View.INVISIBLE);
    }
}