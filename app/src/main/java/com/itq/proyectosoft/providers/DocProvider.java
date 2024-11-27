package com.itq.proyectosoft.providers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itq.proyectosoft.utils.CompressorBitmapImage;

import java.io.File;
import java.util.Date;

public class DocProvider {

    StorageReference mStorageDoc;

    public DocProvider(){
        mStorageDoc = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask saveDocuments(Context context, File photo, Uri ineUri, Uri domUri, String idUser) {
        // Obtener referencia a Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Crear las rutas en Firebase Storage
        String folderPath = "docUsers/" + idUser + "/";
        String photoName = new Date() + ".jpg";
        String ineName = "INE_" + new Date() + ".pdf";
        String domName = "DOM_" + new Date() + ".pdf";

        StorageReference photoRef = storageRef.child(folderPath + photoName);
        StorageReference ineRef = storageRef.child(folderPath + ineName);
        StorageReference domRef = storageRef.child(folderPath + domName);

        // Subir la imagen
        byte[] photoBytes = CompressorBitmapImage.getImage(context, photo.getPath(), 500, 500);
        UploadTask photoUploadTask = photoRef.putBytes(photoBytes);
        handleUploadTask(context, photoUploadTask, photoRef, "photo");

        // Subir el INE
        UploadTask ineUploadTask = ineRef.putFile(ineUri);
        handleUploadTask(context, ineUploadTask, ineRef, "INE");

        // Subir el comprobante de domicilio
        UploadTask domUploadTask = domRef.putFile(domUri);
        handleUploadTask(context, domUploadTask, domRef, "domicilio");

        return domUploadTask;
    }

    private void handleUploadTask(Context context, UploadTask uploadTask, StorageReference ref, String docType) {
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Obtener la URL de descarga y realizar la acción correspondiente
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        String url = downloadUrl.toString();
                        // Guardar la URL en la base de datos o realizar alguna acción
                        Log.d("UploadSuccess", docType + " URL: " + url);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar error de la subida
                Toast.makeText(context, "Error al subir " + docType + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public StorageReference getStorageDoc() {
        return mStorageDoc;
    }
}
