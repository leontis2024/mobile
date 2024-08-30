package com.aula.leontis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TelaCadastro2 extends AppCompatActivity {
    EditText apelido,biografia;
    Spinner sexo;
    TextView infoApelido,erroSexo;
    Boolean generoValido = true;
    Button continuar;
    String nome, sobrenome, email, telefone, dtNasc, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro2);

        apelido = findViewById(R.id.apelido_cadastro);
        infoApelido = findViewById(R.id.info_apelido);

        sexo = findViewById(R.id.sexo_cadastro);
        erroSexo = findViewById(R.id.erro_sexo);

        biografia = findViewById(R.id.biografia_cadastro);

        continuar = findViewById(R.id.btn_continuar);

        //pegando informações do cadastro da tela anterior
        Intent info = getIntent();
        Bundle infoCadastro = info.getExtras();
        if(infoCadastro != null) {

            nome = infoCadastro.getString("nome");
            sobrenome = infoCadastro.getString("sobrenome");
            email = infoCadastro.getString("email");
            telefone = infoCadastro.getString("telefone");
            dtNasc = infoCadastro.getString("dtNasc");
            senha = infoCadastro.getString("senha");
        }


        //adapter para configurar o spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sexo_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapter);

        sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                //verifica se o gênero foi selecionado e não o hint
                if(selectedItem.equals("Selecione seu gênero")){
                    generoValido = false;
                }else{
                    generoValido = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                generoValido = false;
            }
        });

          continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificando se o gênero foi selecionado, se não, da erro
                if(sexo.getSelectedItem().toString().equals("Selecione seu gênero")){
                    erroInput("Selecione seu gênero",erroSexo,sexo);
                    generoValido=false;
                }else{
                    semErroInput(erroSexo,sexo);
                    generoValido=true;
                }
                if(generoValido){
                    //passando as informações de cadastro para a tela de interesses
                    Bundle infoCadastro2 = new Bundle();
                    infoCadastro2.putString("nome",nome);
                    infoCadastro2.putString("sobrenome",sobrenome);
                    infoCadastro2.putString("email",email);
                    infoCadastro2.putString("telefone",telefone);
                    infoCadastro2.putString("dtNasc",dtNasc);
                    infoCadastro2.putString("senha",senha);
                    infoCadastro2.putString("apelido",apelido.getText().toString());
                    infoCadastro2.putString("biografia",biografia.getText().toString());
                    infoCadastro2.putString("sexo",sexo.getSelectedItem().toString());

                    Intent telaCadastroInteresses = new Intent(TelaCadastro2.this, TelaCadastroInteresses.class);
                    telaCadastroInteresses.putExtras(infoCadastro2);
                    startActivity(telaCadastroInteresses);
                }
            }
        });

    }
    //mostra mensagem de erro
    public void erroInput(String mensagem, TextView texto, Spinner input){
        input.setBackground(ContextCompat.getDrawable(TelaCadastro2.this, R.drawable.input_erro));
        //input.setHintTextColor(ContextCompat.getColor(TelaCadastro2.this, R.color.vermelho_erro_hint));
        //mostra mensagem de erro
        texto.setText(mensagem);
        texto.setVisibility(View.VISIBLE);
    }
    //oculta mensagem de erro
    public void semErroInput(TextView erro, Spinner input){
        input.setBackground(ContextCompat.getDrawable(TelaCadastro2.this, R.drawable.input));
      //  input.setHintTextColor(ContextCompat.getColor(TelaCadastro2.this, R.color.hint));
        erro.setVisibility(View.INVISIBLE);
    }
}