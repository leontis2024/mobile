package com.aula.leontis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aula.leontis.R;

import java.util.Calendar;

public class TelaCadastro extends AppCompatActivity {
    ImageButton btCalendar,btnOlho1, btnOlho2;
    EditText nome,sobrenome,email,telefone,dtNasc,senha,senha2;
    Button continuar;
    boolean valida=false;
    TextView erroNome,erroSobrenome,erroEmail,erroTelefone,erroDtNasc,erroSenha,erroSenha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);
        btnOlho1 = findViewById(R.id.verSenha1);
        btnOlho2 = findViewById(R.id.verSenha2);


        nome = findViewById(R.id.nome_cadastro);
        erroNome = findViewById(R.id.erro_nome_cadastro);

        sobrenome = findViewById(R.id.sobrenome_cadastro);
        erroSobrenome = findViewById(R.id.erro_sobrenome_cadastro);

        email = findViewById(R.id.email_cadastro);
        erroEmail = findViewById(R.id.erro_email_cadastro);

        telefone = findViewById(R.id.telefone_cadastro);



        telefone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }

                String unmasked = s.toString().replaceAll("[^\\d]", "");
                StringBuilder formatted = new StringBuilder();

                int length = unmasked.length();

                if (length > 0) {
                    formatted.append("(");
                    formatted.append(unmasked.substring(0, Math.min(2, length)));
                }

                if (length > 2) {
                    formatted.append(")");
                    formatted.append(unmasked.substring(2, Math.min(7, length)));
                }

                if (length > 7) {
                    formatted.append("-");
                    formatted.append(unmasked.substring(7, Math.min(11, length)));
                }

                isUpdating = true;
                telefone.setText(formatted.toString());
                telefone.setSelection(formatted.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        erroTelefone = findViewById(R.id.erro_telefone_cadastro);

        dtNasc = findViewById(R.id.data_nasc_cadastro);

        dtNasc.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;
            private String previous = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Verificar se já estamos atualizando para evitar loops infinitos
                if (isUpdating) {
                    return;
                }

                String current = s.toString().replaceAll("[^\\d]", ""); // Remover caracteres não numéricos

                // Verificar se o usuário está apagando o texto
                if (current.equals(previous)) {
                    return;
                }

                StringBuilder formatted = new StringBuilder();

                // Inserir a barra após os dois primeiros dígitos
                if (current.length() >= 2) {
                    formatted.append(current.substring(0, 2)).append("/");
                } else if (current.length() > 0) {
                    formatted.append(current);
                }

                // Inserir a barra após os quatro dígitos
                if (current.length() >= 4) {
                    formatted.append(current.substring(2, 4)).append("/");
                } else if (current.length() > 2) {
                    formatted.append(current.substring(2));
                }

                // Adicionar o ano completo
                if (current.length() > 4) {
                    formatted.append(current.substring(4));
                }

                // Prevenir o loop de atualização
                isUpdating = true;
                dtNasc.setText(formatted.toString());
                dtNasc.setSelection(formatted.length()); // Colocar o cursor no final
                isUpdating = false;

                previous = current;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        erroDtNasc = findViewById(R.id.erro_nasc_cadastro);

        senha = findViewById(R.id.senha_cadastro);
        erroSenha = findViewById(R.id.erro_senha_cadastro);

        btnOlho1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnOlho1.getContentDescription().equals("fechado")) {

                    if (senha != null && !(senha.getText().equals(""))) {
                        btnOlho1.setContentDescription("aberto");
                        btnOlho1.setImageResource(R.drawable.olhinho);
                        senha.setInputType(InputType.TYPE_CLASS_TEXT);
                        senha.setSelection(senha.length());
                    }
                }else{

                    if (senha != null && !(senha.getText().equals(""))) {
                        btnOlho1.setContentDescription("fechado");
                        btnOlho1.setImageResource(R.drawable.olhinho_fechado);
                        senha.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        senha.setSelection(senha.length());
                    }
                }
            }
        });


        senha2 = findViewById(R.id.senha_cadastro2);
        erroSenha2 = findViewById(R.id.erro_senha_cadastro2);

        btnOlho2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnOlho2.getContentDescription().equals("fechado")) {

                    if (senha2 != null && !(senha2.getText().equals(""))) {
                        btnOlho2.setContentDescription("aberto");
                        btnOlho2.setImageResource(R.drawable.olhinho);
                        senha2.setInputType(InputType.TYPE_CLASS_TEXT);
                        senha2.setSelection(senha2.length());
                    }
                }else{

                    if (senha2 != null && !(senha2.getText().equals(""))) {
                        btnOlho2.setContentDescription("fechado");
                        btnOlho2.setImageResource(R.drawable.olhinho_fechado);
                        senha2.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        senha2.setSelection(senha2.length());
                    }
                }
            }
        });


        continuar = findViewById(R.id.btn_continuar_cadastro);
        btCalendar = findViewById(R.id.calendario_cadastro);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificando se os input estão vazios, se sim da erro
                if(nome.getText().toString()==null || nome.getText().toString().equals("")){
                    erroInput("Digite seu nome",erroNome,nome);
                }else if(nome.getText().length() < 3 || nome.getText().length() > 100){
                    erroInput("O nome deve ter mais que 3 caracteres e menos que 100",erroNome,nome);
                }else{
                    semErroInput(erroNome,nome);
                }

                if(sobrenome.getText().toString()==null || sobrenome.getText().toString().equals("")){
                    erroInput("Digite seu sobrenome",erroSobrenome,sobrenome);
                }else if(sobrenome.getText().length() < 3 || sobrenome.getText().length() > 100){
                    erroInput("O sobrenome deve ter mais que 3 caracteres e menos que 100",erroSobrenome,sobrenome);
                }else{
                    semErroInput(erroSobrenome,sobrenome);
                }

                if(email.getText().toString()==null || email.getText().toString().equals("")) {
                    erroInput("Digite seu e-mail",erroEmail,email);
                }else if(!emailValido(email.getText().toString())) {
                    //verificando se o e-mail é válido
                    erroInput("Digite um e-mail válido", erroEmail, email);
                }else{
                    semErroInput(erroEmail,email);
                }

                if(telefone.getText().toString()==null || telefone.getText().toString().equals("")){
                    erroInput("Digite seu telefone",erroTelefone,telefone);
                }else if(telefone.getText().length() < 14 || telefone.getText().length() > 14){
                    erroInput("Digite o telefone no formato (XX)XXXXX-XXXX",erroTelefone,telefone);
                }else{
                    semErroInput(erroTelefone,telefone);
                }


                String dataNascimento = dtNasc.getText().toString();
                if (dataNascimento.equals("")) {
                    erroInput("Digite sua data de nascimento", erroDtNasc, dtNasc);
                } else {
                    try {
                        // Extrair o dia, mês e ano da string
                        String[] partes = dataNascimento.split("/");
                        if (partes.length != 3) {
                            erroInput("Formato de data inválido. Use dd/mm/aaaa", erroDtNasc, dtNasc);
                            valida = false;
                            return;
                        }

                        int diaNascimento = Integer.parseInt(partes[0]);
                        int mesNascimento = Integer.parseInt(partes[1]);
                        int anoNascimento = Integer.parseInt(partes[2]);

                        // Obter o ano atual
                        Calendar calendar = Calendar.getInstance();
                        int anoAtual = calendar.get(Calendar.YEAR);

                        // Calcular a data mínima de nascimento (10 anos atrás)
                        int anoMinimo = anoAtual - 10;  // Para garantir que a pessoa tenha pelo menos 10 anos
                        int anoMaximo = anoAtual - 100; // Para garantir que a pessoa não tenha mais de 100 anos

                        // Verificar se o ano de nascimento é válido
                        if (anoNascimento > anoAtual) {
                            erroInput("Digite uma data de nascimento válida", erroDtNasc, dtNasc);
                            valida = false;
                        } else if (anoNascimento < anoMaximo) {
                            erroInput("Você deve ter no máximo 100 anos", erroDtNasc, dtNasc);
                            valida = false;
                        } else if (anoNascimento > anoMinimo) {
                            erroInput("Você deve ter no mínimo 10 anos", erroDtNasc, dtNasc);
                            valida = false;
                        }
                        else {
                            // Verificar se o mês é válido
                            if (mesNascimento < 1 || mesNascimento > 12) {
                                erroInput("Mês inválido. Deve ser entre 01 e 12", erroDtNasc, dtNasc);
                                valida = false;
                            } else {
                                // Verificar se o dia é válido para o mês e ano dados
                                boolean diaValido = false;
                                if (mesNascimento == 2) {
                                    // Verifica se é um ano bissexto
                                    if ((anoNascimento % 4 == 0 && anoNascimento % 100 != 0) || (anoNascimento % 400 == 0)) {
                                        diaValido = diaNascimento >= 1 && diaNascimento <= 29; // Fevereiro bissexto
                                    } else {
                                        diaValido = diaNascimento >= 1 && diaNascimento <= 28; // Fevereiro normal
                                    }
                                } else if (mesNascimento == 4 || mesNascimento == 6 || mesNascimento == 9 || mesNascimento == 11) {
                                    diaValido = diaNascimento >= 1 && diaNascimento <= 30; // Meses com 30 dias
                                } else {
                                    diaValido = diaNascimento >= 1 && diaNascimento <= 31; // Meses com 31 dias
                                }

                                if (!diaValido) {
                                    erroInput("Dia inválido para o mês indicado", erroDtNasc, dtNasc);
                                    valida = false;
                                } else {
                                    semErroInput(erroDtNasc, dtNasc);
                                    valida = true;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        erroInput("Formato de data inválido", erroDtNasc, dtNasc);
                        valida = false;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        erroInput("Formato de data inválido. Use dd/mm/aaaa", erroDtNasc, dtNasc);
                        valida = false;
                    }
                }


                if(senha.getText().toString()==null || senha.getText().toString().equals("")){
                    erroInput("Digite sua senha",erroSenha,senha);
                }else if(senha.getText().length()<6 || senha.getText().length()>20){
                    erroInput("A senha deve ter no mínimo 6 caracteres e no máximo 20",erroSenha,senha);
                }else{
                    semErroInput(erroSenha,senha);
                }

                if(senha2.getText().toString()==null || senha2.getText().toString().equals("")){
                    erroInput("Confirme sua senha",erroSenha2,senha2);
                }else if(!senha.getText().toString().equals(senha2.getText().toString())){
                    erroInput("As senhas devem ser iguais",erroSenha2,senha2);
                }else{
                    semErroInput(erroSenha2,senha2);
                }

                if(informacoesValidas()){
                    //caso não de erro, cria um bundle com as informações de login e passa para a tela de cadastro2
                    Bundle infoCadastro = new Bundle();
                    infoCadastro.putString("nome",nome.getText().toString());
                    infoCadastro.putString("sobrenome",sobrenome.getText().toString());
                    infoCadastro.putString("email",email.getText().toString());
                    infoCadastro.putString("dtNasc",dtNasc.getText().toString());
                    infoCadastro.putString("telefone",telefone.getText().toString());
                    infoCadastro.putString("senha",senha.getText().toString());

                    Intent telaCadastro2 = new Intent(TelaCadastro.this, TelaCadastro2.class);
                    telaCadastro2.putExtras(infoCadastro);
                    startActivity(telaCadastro2);
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
    //Verifica se todos os campos estão sem erro
    public boolean informacoesValidas(){
        boolean nomeValido = true;
        boolean sobrenomeValido = true;
        boolean emailValido = true;
        boolean telefoneValido = true;
        boolean dtNascValido = valida;
        boolean senhaValido = true;
        boolean senha2Valido = true;

        if(nome.getBackground().equals(R.drawable.input_erro)){
            nomeValido = false;
        }
        if(nome.getText().toString().equals("")){
            nomeValido = false;
        }
        if(nome.getText().length() < 3 || nome.getText().length() > 100) {
            nomeValido = false;
        }

        if(sobrenome.getBackground().equals(R.drawable.input_erro)){
            sobrenomeValido = false;
        }
        if(sobrenome.getText().toString().equals("")){
            sobrenomeValido = false;
        }
        if(sobrenome.getText().length() < 3 || sobrenome.getText().length() > 100){
            sobrenomeValido = false;
        }

        if(email.getBackground().equals(R.drawable.input_erro)){
            emailValido = false;
        }
        if(email.getText().toString().equals("")){
            emailValido = false;
        }
        if(!emailValido(email.getText().toString())){
            emailValido = false;
        }

        if(telefone.getBackground().equals(R.drawable.input_erro)){
            telefoneValido = false;
        }
        if(telefone.getText().toString().equals("")){
            telefoneValido = false;
        }
        if(telefone.getText().length() < 14 || telefone.getText().length() > 14){
            telefoneValido = false;
        }

        if(dtNasc.getBackground().equals(R.drawable.input_erro)){
            dtNascValido = false;
        }
        if(dtNasc.getText().toString().equals("")){
            dtNascValido = false;
        }

        if(senha.getBackground().equals(R.drawable.input_erro)){
            senhaValido = false;
        }
        if(senha.getText().toString().equals("")){
            senhaValido = false;
        }
        if(senha.getText().length()<6|| senha.getText().length()>20){
            senhaValido = false;
        }

        if(senha2.getBackground().equals(R.drawable.input_erro)){
            senha2Valido = false;
        }
        if(senha2.getText().toString().equals("")){
            senha2Valido = false;
        }
        if(!senha.getText().toString().equals(senha2.getText().toString())){
            senha2Valido = false;
        }

        return nomeValido && sobrenomeValido && emailValido && telefoneValido && dtNascValido && senhaValido && senha2Valido;
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