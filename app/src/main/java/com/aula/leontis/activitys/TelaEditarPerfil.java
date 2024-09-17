package com.aula.leontis.activitys;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.leontis.R;
import com.aula.leontis.models.usuario.Usuario;
import com.aula.leontis.services.UsuarioService;
import com.aula.leontis.utilities.DataBaseFotos;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TelaEditarPerfil extends AppCompatActivity {
    EditText apelido,biografia,nome,telefone,dtNasc,sobrenome;
    ImageView fotoPerfil;
    ImageButton btnVoltar,btnFinalizar,btCalendar,btnMudarFotos;
    DataBaseFotos dataBase = new DataBaseFotos();
    ProgressBar carregando;
    UsuarioService usuarioService = new UsuarioService();
    String id;
    Spinner sexo;
    TextView erroUsuarioEditar, sexoTxt;
    String txtSexo, urlFoto;

    Uri imagemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_editar_perfil);
        apelido = findViewById(R.id.apelido);
        biografia = findViewById(R.id.biografia);
        fotoPerfil = findViewById(R.id.fotoPerfil);
        nome = findViewById(R.id.nome_editar);
        sobrenome = findViewById(R.id.sobrenome_editar);
        telefone = findViewById(R.id.telefone_editar);
        dtNasc = findViewById(R.id.data_nasc_editar);
        btCalendar = findViewById(R.id.calendario_editar);
        carregando = findViewById(R.id.progressBar);
        btnMudarFotos = findViewById(R.id.btnMudarFotos);
        sexoTxt = findViewById(R.id.sexoTxt);
        
        btnVoltar = findViewById(R.id.btnVoltarEdit);
        btnFinalizar = findViewById(R.id.btnFinalizarEdicao);
        sexo = findViewById(R.id.sexo_editar);
        erroUsuarioEditar = findViewById(R.id.erroUsuarioEdit);

        Bundle info = getIntent().getExtras();
        if(info!=null) {
            id = info.getString("id");
            usuarioService.selecionarUsuarioPorId(id,this,apelido,biografia,fotoPerfil,nome,sobrenome,telefone,sexoTxt,dtNasc,erroUsuarioEditar);
        }
        if(sexoTxt.getText().toString().equals("Masculino")){
            sexo.setSelection(2);
        }else if(sexoTxt.getText().toString().equals("Feminino")){
            sexo.setSelection(1);
        }else if(sexoTxt.getText().toString().equals("Outro")){
            sexo.setSelection(4);
        }else {
            sexo.setSelection(3);
        }
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TelaEditarPerfil.this, null, year, month, dayOfMonth);

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

        final Boolean[] generoValido = {false};
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
                    generoValido[0] = false;
                }else{
                    generoValido[0] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                generoValido[0] = false;
            }
        });
        btnMudarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                resultLauncherGaleria.launch(intent);
            }
        });

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(generoValido[0]==false) {
                    erroInput2(sexo);
                    erroUsuarioEditar.setText("Selecione seu gênero");
                    erroUsuarioEditar.setVisibility(View.VISIBLE);

                }else{
                    semErroInput2(sexo);
                }

                if(nome.getText().toString().equals("")){
                    erroInput("Preencha o campo nome",erroUsuarioEditar,nome);
                }else if(nome.getText().length() < 3 || nome.getText().length() > 100){
                    erroInput("O nome deve ter mais que 3 caracteres e menos que 100",erroUsuarioEditar,nome);
                }else{
                    semErroInput(null,nome);
                }

                if(sobrenome.getText().toString().equals("")){
                    erroInput("Preencha o campo sobrenome",erroUsuarioEditar,sobrenome);
                }else if(sobrenome.getText().length() < 3 || sobrenome.getText().length() > 100){
                    erroInput("O sobrenome deve ter mais que 3 caracteres e menos que 100",erroUsuarioEditar,sobrenome);
                }else{
                    semErroInput(null,sobrenome);
                }

                if(telefone.getText().toString().equals("")){
                    erroInput("Preencha o campo telefone",erroUsuarioEditar,telefone);
                }else if(telefone.getText().length() < 11 || telefone.getText().length() > 11){
                    erroInput("Digite o telefone com o DDD sem espaços",erroUsuarioEditar,telefone);
                }else{
                    semErroInput(null,telefone);
                }

                if(dtNasc.getText().toString().equals("")){
                    erroInput("Preencha o campo data de nascimento",erroUsuarioEditar,dtNasc);
                }else{
                    semErroInput(null,dtNasc);
                }

                if(informacoesValidas()){
                    erroUsuarioEditar.setVisibility(View.INVISIBLE);
                    Toast.makeText(TelaEditarPerfil.this, "foiii", Toast.LENGTH_SHORT).show();
                    Map<String,Object> update = new HashMap<>();
                    update.put("apelido",apelido.getText().toString());
                    update.put("biografia",biografia.getText().toString());
                    update.put("sexo",sexo.getSelectedItem().toString().substring(0,1));

                    LocalDate data = LocalDate.parse(dtNasc.getText().toString());
                    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String dataFormatada = data.format(formatador);

                    update.put("dataNascimento",dataFormatada);
                    update.put("telefone",formatarNumero(telefone.getText().toString()));
                    update.put("sobrenome",sobrenome.getText().toString());
                    update.put("nome",nome.getText().toString());
                    update.put("urlImagem",urlFoto);
                  //  usuarioService.atualizarUsuario(id,update);
                }
            }
        });

    }
    //mostra mensagem de erro
    public void erroInput(String mensagem, TextView texto, EditText input){
        input.setBackground(ContextCompat.getDrawable(TelaEditarPerfil.this, R.drawable.input_erro));
        input.setHintTextColor(ContextCompat.getColor(TelaEditarPerfil.this, R.color.vermelho_erro_hint));
        //mostra mensagem de erro
        texto.setText(mensagem);
        texto.setVisibility(View.VISIBLE);
    }
    //oculta mensagem de erro
    public void semErroInput(TextView erro, EditText input){
        input.setBackground(ContextCompat.getDrawable(TelaEditarPerfil.this, R.drawable.input));
        input.setHintTextColor(ContextCompat.getColor(TelaEditarPerfil.this, R.color.hint));
        if(erro !=null) {
            erro.setVisibility(View.INVISIBLE);
        }
    }
    //mostra mensagem de erro
    public void erroInput2(Spinner input) {
        input.setBackground(ContextCompat.getDrawable(TelaEditarPerfil.this, R.drawable.input_erro));
    }
    //oculta mensagem de erro
    public void semErroInput2(Spinner input){
        input.setBackground(ContextCompat.getDrawable(TelaEditarPerfil.this, R.drawable.input));
    }
    public boolean informacoesValidas(){
        boolean nomeValido = true;
        boolean sobrenomeValido = true;
        boolean telefoneValido = true;
        boolean dtNascValido = true;
        boolean sexoValido = true;

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

        if(telefone.getBackground().equals(R.drawable.input_erro)){
            telefoneValido = false;
        }
        if(telefone.getText().toString().equals("")){
            telefoneValido = false;
        }
        if(telefone.getText().length() < 11 || telefone.getText().length() > 11){
            telefoneValido = false;
        }

        if(dtNasc.getBackground().equals(R.drawable.input_erro)){
            dtNascValido = false;
        }
        if(dtNasc.getText().toString().equals("")){
            dtNascValido = false;
        }

        if(sexo.getBackground().equals(R.drawable.input_erro)){
            sexoValido = false;
        }
        if(sexo.getSelectedItem().toString().equals("Selecione seu gênero")){
            sexoValido = false;
        }
        

        return nomeValido && sobrenomeValido && telefoneValido && dtNascValido && sexoValido;
    }
    public String formatarNumero(String phoneNumber) {
        // Formata o número
        String formato = String.format("(%s)%s-%s",
                phoneNumber.substring(0, 2),    // DDD
                phoneNumber.substring(2, 7),    // Parte do número antes do hífen
                phoneNumber.substring(7));      // Parte do número depois do hífen
        return formato;
    }
    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                imagemUri = result.getData().getData();
                if(imagemUri != null){
                    //Glide para carregar a imagem no firebase
                    Glide.with(this)
                            .load(imagemUri)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    // acessar o Drawable e convertê-lo em Bitmap
                                    Bitmap bitmap = drawableToBitmap(resource);

                                    //falta cadastrar usuario genero

                                    Toast.makeText(TelaEditarPerfil.this, "Aguarde fazemos o upload da imagem...", Toast.LENGTH_SHORT).show();
                                    carregando.setVisibility(View.VISIBLE);

                                    Handler esperar = new Handler();
                                    esperar.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(!(id.equals(""))) {
                                                // upload do Bitmap para o Firebase Storage retornando a url dela
                                                dataBase.subirFotoUsuario(TelaEditarPerfil.this, bitmap, id).addOnSuccessListener(new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(String url) {
                                                        // Aqui você pode usar a URL da imagem
                                                        Log.d("URL", "URL da imagem: " + url);
                                                        urlFoto = url;
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("URL", "Erro ao obter a URL da imagem", e);

                                                    }
                                                });
                                            }
                                        }
                                    }, 5000);
                                }
                            });

                    //Glide para carregar a imagem na tela
                    Glide.with(this)
                            .load(imagemUri)
                            .circleCrop()
                            .into(fotoPerfil);

                    Handler handler = new Handler();

                    //Esperando 10 segundos para abrir a tela de bem-vindo
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            carregando.setVisibility(View.GONE);
                            atualizarUsuarioFirebase(imagemUri);
                        }
                    }, 5000);

                }
            });

    //método que converte uma imagem em Bitmap
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public void atualizarUsuarioFirebase(Uri foto){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()
                .setPhotoUri(foto)
                .build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Cadastro","Sucesso");
                }
            }
        });
    }
}