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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DataBaseFotos {
    public void subirFotoUsuario(Context c, Bitmap foto, String idUser) {
        // Verifique se o ImageView contém uma imagem
        if (foto == null) {
            Toast.makeText(c, "Imagem não encontrada!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversão da imagem para Bitmap
//        Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] databyte = baos.toByteArray();

        // Abrir Database
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference("usuarios").child("fotouser_" + idUser + ".jpg")
                .putBytes(databyte)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(c, "Upload feito com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(c, "Upload falhou!", Toast.LENGTH_SHORT).show();
                        Log.d("upload", e.getMessage());
                    }
                });
    }


}
