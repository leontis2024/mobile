package com.aula.leontis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
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
    //Método para subir a foto de perfil do usuário no firebase
//    public void subirFotoUsuario(Context c, Bitmap foto, String idUser) {
//        // Verifique se o ImageView contém uma imagem
//        if (foto == null) {
//            Toast.makeText(c, "Imagem não encontrada!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Conversão da imagem para Bitmap
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        foto.compress(Bitmap.CompressFormat.JPEG, 70, baos);
//        byte[] databyte = baos.toByteArray();
//
//        // Abrir Database
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        storage.getReference("usuarios").child("fotouser_" + idUser + ".jpg")
//                .putBytes(databyte)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(c, "Upload feito com sucesso!", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(c, "Upload falhou!", Toast.LENGTH_SHORT).show();
//                        Log.d("upload", e.getMessage());
//                    }
//                });
//    }
//

    public Task<String> subirFotoUsuario(Context c, Bitmap foto, String idUser) {
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
        StorageReference storageRef = storage.getReference("usuarios").child("fotouser_" + idUser + ".jpg");

        // Faça o upload da imagem
        storageRef.putBytes(databyte)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(c, "Upload feito com sucesso!", Toast.LENGTH_SHORT).show();

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
