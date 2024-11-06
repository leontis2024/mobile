package com.aula.leontis.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
//Classe para manipulação de imagem do firebase storage
public class DataBaseFotos {
       public Task<String> subirFoto(Context c, Bitmap foto, String idUser,String path, String nome) {
           if(path.equals("scanner")){
               Bitmap foto2 = foto;
               int proporcaoWidth = (int) (foto2.getWidth() * 0.5);
               int proporcaoHeight = (int) (foto2.getHeight() * 0.5);
               int x = (foto2.getWidth() - proporcaoWidth) / 2;
               int y = (foto2.getHeight() - proporcaoHeight) / 2;
               Bitmap fotoCortada = Bitmap.createBitmap(foto2, x, y, proporcaoWidth, proporcaoHeight);
               foto = fotoCortada;
           }
        // Criação do TaskCompletionSource para retornar a URL
        TaskCompletionSource<String> tcs = new TaskCompletionSource<>();

        // Verifique se a foto é nula
        if (foto == null) {
            Toast.makeText(c, "Imagem não encontrada!", Toast.LENGTH_SHORT).show();
            tcs.setException(new Exception("Imagem não encontrada!"));
            return tcs.getTask();
        }

        // Conversão da imagem para ByteArray
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] databyte = baos.toByteArray();

        // Referência ao Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference(path).child(nome+ idUser + ".jpg");

        // Faça o upload da imagem
        storageRef.putBytes(databyte)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       // Toast.makeText(c, "Upload feito com sucesso!", Toast.LENGTH_SHORT).show();
                        Log.d("UPLOAD", "Upload feito com sucesso!");

                        // Após o upload, obtenha a URL de download
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Retorna a URL
                                tcs.setResult(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(c, "Falha ao obter a URL!", Toast.LENGTH_SHORT).show();
                                tcs.setException(e);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(c, "Upload falhou!", Toast.LENGTH_SHORT).show();
                        tcs.setException(e);
                    }
                });

        return tcs.getTask();
    }

}
