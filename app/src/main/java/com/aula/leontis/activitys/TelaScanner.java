package com.aula.leontis.activitys;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.leontis.R;
import com.aula.leontis.services.ModeloService;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.utilities.DataBaseFotos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelaScanner extends AppCompatActivity {
    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.CAMERA,
    };
    ImageButton btnEscanear, btnVoltar;
    TextView erro;

    DataBaseFotos dataBaseFotos = new DataBaseFotos();
    ModeloService modeloService  = new ModeloService();
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private androidx.camera.view.PreviewView viewFinder,viewFinder2;
    String id;
    ImageView foto;
    ObraService obraService = new ObraService();
    ProgressBar carregar;
    String urlFoto,idGuia;
    int nrOrdem;
    TextView idObra;
    ImageView borda;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_scanner);
        cameraExecutor = Executors.newSingleThreadExecutor();
        carregar = findViewById(R.id.carregar);
        idObra = findViewById(R.id.idObra);
        borda = findViewById(R.id.borda);

        btnEscanear = findViewById(R.id.btnEscanear);
        btnVoltar = findViewById(R.id.btnFiltrar);
        foto = findViewById(R.id.foto);
        viewFinder = findViewById(R.id.viewFinder);;
        erro = findViewById(R.id.erroScan);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id= bundle.getString("id");
            idGuia = bundle.getString("idGuia");
            nrOrdem = bundle.getInt("nrOrdem");
        }


        if(allPermissionsGranted()){
            startCamera();
        }else{
            requestPermissions();
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erro.setTextColor(ContextCompat.getColor(TelaScanner.this, R.color.bege_claro));
                carregar.setVisibility(View.VISIBLE);
                erro.setText("Escaneando...");
                erro.setVisibility(View.VISIBLE);
                takePhoto();
            }
        });

    }
    private void takePhoto() {
        if(imageCapture == null){
            return;
        }
        //definir o nome e caminho para a imagem
        String nome = "scan"+System.currentTimeMillis()+".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, nome);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Leontis");

        //Carregar imagem com as config
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        ).build();

        //orientação da imagem
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation){
                int rotation;
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                }else if(orientation >= 135 && orientation <= 224){
                    rotation = Surface.ROTATION_180;
                }else if(orientation >= 255 && orientation <= 314){
                    rotation = Surface.ROTATION_90;
                }else{
                    rotation = Surface.ROTATION_0;
                }
                imageCapture.setTargetRotation(rotation);
            }
        };

        orientationEventListener.enable();

        //Salvar imagem
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                foto.setImageURI(outputFileResults.getSavedUri());

                dataBaseFotos.subirFoto( TelaScanner.this,drawableToBitmap(foto.getDrawable()), id,"scanner","scan").addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String url) {
                        carregar.setVisibility(View.INVISIBLE);
                        erro.setVisibility(View.INVISIBLE);

                        modeloService.preditarObra(url,TelaScanner.this,id,idObra,idGuia,nrOrdem,erro,borda);

                        Log.d("URL SCAN", "URL da imagem: " + url);
                        urlFoto = url;
                        if (outputFileResults.getSavedUri() != null) {
                            getContentResolver().delete(outputFileResults.getSavedUri(), null, null);
                            Log.d("EXCLUIR_FOTO", "Imagem excluída da galeria: " + outputFileResults.getSavedUri().toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("URL SCAN ERRO", "Erro ao obter a URL da imagem", e);

                    }
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e("CAMERA","ERRO"+exception.getMessage());
                Toast.makeText(TelaScanner.this, "Não foi possível salvar a foto", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());


                // ImageCapture
                imageCapture = new ImageCapture.Builder().build();

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll();

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                            this,
                            cameraSelector,
                            preview,
                            imageCapture
                    );
                } catch (Exception exc) {
                    Log.e(TAG, "Camera binding failed", exc);
                }

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private void requestPermissions(){
        //request de permissão
        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }
    private ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                // Handle Permission granted/rejected
                boolean permissionGranted = true;
                for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                    if (Arrays.asList(REQUIRED_PERMISSIONS).contains(entry.getKey()) && !entry.getValue()) {
                        permissionGranted = false;
                        break;
                    }
                }
                if (!permissionGranted) {
                    Toast.makeText(getApplicationContext(),"Permissão NEGADA. Tente novamente",Toast.LENGTH_SHORT).show();
                } else {
                    startCamera(); ;
                }
            });
    private boolean allPermissionsGranted() {
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this,permission)
                    != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
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
}