package com.itq.proyectosoft.providers;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itq.proyectosoft.utils.CompressorBitmapImage;
import android.content.Context;

import java.io.File;
import java.util.Date;

public class imageProvider {

    StorageReference mStorage;
    public imageProvider() {
        mStorage = FirebaseStorage.getInstance().getReference(); //REFERENCIA AL STROAGE DE FIRABASE DONDE SE GUARDARAN LAS IMAGENES
    }

    public UploadTask save(Context context, File file) { //AQUI SE MANDA LA IMAGEN PARA GUARDARSE
        byte[] imageByte = CompressorBitmapImage.getImage(context, file.getPath(), 500, 500); //SE COMPRIME LA IMAGEN
        String folderPath = "posts/";
        String imageName = new Date() + ".jpg";
        StorageReference storage = mStorage.child(folderPath + imageName);//SE CREA UNA REFERENCIA AL STORAGE DE FIREBASE
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte); //SE GUARDA LA IMAGEN EN EL STORAGE DE FIREBASE
        return task;
    }

    public StorageReference getStorage() {
        return mStorage;
    }

}
