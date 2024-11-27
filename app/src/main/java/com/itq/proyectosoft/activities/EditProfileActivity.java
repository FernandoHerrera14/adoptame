package com.itq.proyectosoft.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.media.ExifInterface;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.models.Posts;
import com.itq.proyectosoft.models.User;
import com.itq.proyectosoft.providers.AuthProvider;
import com.itq.proyectosoft.providers.UserProvider;
import com.itq.proyectosoft.providers.imageProvider;
import com.itq.proyectosoft.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewProfile;
    ImageView mImageViewPortada;
    TextInputEditText mTextInputCorreo;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputPass;
    TextInputEditText mTextInputCorreoNuevo;
    TextView mTextViewUserName;
    TextView mTextViewCorreo;
    ImageButton imgBtnPhotoProfile;
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;
    File mImageFile;
    imageProvider mImageProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    String email = "";
    String phone = "";
    String newEmail = "";
    String user = "";
    String lastName = "";
    String pass = "";

    String mPhotoProfile = "";
    Button btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mCircleImageViewProfile = findViewById(R.id.circleImageEditProfile);
        mImageViewPortada = findViewById(R.id.imgViewPortada);

        mTextInputPhone = findViewById(R.id.textInputPhone);
        mTextInputPass = findViewById(R.id.textInputPass);
        mTextInputCorreoNuevo = findViewById(R.id.textInputEmailNuevo);
        mTextViewUserName = findViewById(R.id.txtViewNombrePerfil);
        mTextViewCorreo = findViewById(R.id.txtViewCorreoPerfil);

        imgBtnPhotoProfile = findViewById(R.id.ll_edit_PhotoProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        mImageProvider = new imageProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        imgBtnPhotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoSeleccion();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });

        getUser();
    }

    private void getUser() {
        mUserProvider.getUser(mAuthProvider.getYouId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    email = documentSnapshot.getString("email");
                    phone = documentSnapshot.getString("telefono");
                    user = documentSnapshot.getString("userName");
                    lastName = documentSnapshot.getString("apellidos");
                    mPhotoProfile = documentSnapshot.getString("img_profile");
                    String completeName = user + " " + lastName;
                    mTextViewUserName.setText(completeName);
                    mTextInputPhone.setText(phone);
                    mTextViewCorreo.setText(email);
                    String imgProfile = documentSnapshot.getString("img_profile");
                    Picasso.get().load(imgProfile).into(mCircleImageViewProfile);
                }
            }
        });
    }
    private void EditProfile() {
       phone = mTextInputPhone.getText().toString();
       pass = mTextInputPass.getText().toString();
       newEmail = mTextInputCorreoNuevo.getText().toString();
       if (!email.isEmpty() && !phone.isEmpty() && !pass.isEmpty()){
           if(mImageFile != null)
               saveImage(mImageFile);
           else if (mPhotoFile != null) {
               saveImage(mPhotoFile);
           }else {
               Toast.makeText(this, "Necesitas Foto", Toast.LENGTH_SHORT).show();
           }
       }else {
           Toast.makeText(this, "Si deseas editar debes llenar los campos", Toast.LENGTH_SHORT).show();
       }
    }
    private void saveImage(File imageFile)
    {
        mImageProvider.save(EditProfileActivity.this, imageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String urlImgProfile = uri.toString();
                            User user2 = new User();
                            user2.setId(mAuthProvider.getYouId());
                            user2.setEmail(newEmail);
                            user2.setTelefono(phone);
                            user2.setImgProfile(urlImgProfile);


                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null) {
                                AuthCredential credential = EmailAuthProvider.getCredential(email, pass);
                                Log.d( "cambio","User re-authenticated. " + user2.getEmail() + ", " + email );
                                user.reauthenticate(credential) .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //Now change your email address \\
                                        //----------------Code for Changing Email Address----------\\
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user.updateEmail(newEmail)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mUserProvider.updateU2(user2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> taskPost) {
                                                                    if (taskPost.isSuccessful()) {
                                                                        Toast.makeText(EditProfileActivity.this, "Actualizacion completa", Toast.LENGTH_LONG).show();
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(EditProfileActivity.this, "Error en la actulizacion", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                        //----------------------------------------------------------\\
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "La imagen no se pudo guardar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mostrarDialogoSeleccion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Foto de perfil");
        builder.setItems(new CharSequence[]{"Tomar foto", "Seleccionar de galería"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openCamara();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT);
        openGallery.setType("image/*");
        launcher.launch(openGallery);
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                try {
                                    mImageFile = FileUtil.from(EditProfileActivity.this, result.getData().getData());
                                    mCircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else
                                Toast.makeText(EditProfileActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    });

    private void openCamara(){
        Intent openCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (openCamara.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
            }catch(Exception e){
                Toast.makeText(this, "Error en el archivo "+e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(EditProfileActivity.this,"com.itq.proyectosoft", photoFile);
                openCamara.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                launcherCamara.launch(openCamara);
            }
        }
    }
    ActivityResultLauncher<Intent> launcherCamara = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                try {
                                    mPhotoFile = new File(mAbsolutePhotoPath);
                                    Picasso.get().load(mPhotoPath).into(mCircleImageViewProfile);
                                    Log.e("fotoProfile", "listo perfil");

                                    try {
                                        Bitmap imageBitmap = BitmapFactory.decodeFile(mAbsolutePhotoPath);

                                        // Rotar la imagen si es necesario
                                        int rotation = getRotationAngle(mAbsolutePhotoPath);
                                        if (rotation != 0) {
                                            Matrix matrix = new Matrix();
                                            matrix.postRotate(rotation);
                                            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
                                            mCircleImageViewProfile.setImageBitmap(imageBitmap);
                                        }else
                                            Log.e("errorCamaraFrontal", "NO ");

                                        // Mostrar la imagen en el CircleImageView

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Log.e("errorProfile2chat", "NO " + ex);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("errorProfile2", "NO "+e);
                                }
                            }
                            else
                                Toast.makeText(EditProfileActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    });

    private File createPhotoFile() throws IOException{
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "photo",
                ".jpg",
                storageDir
        );
        mPhotoPath = "file:" + photoFile.getAbsolutePath();
        mAbsolutePhotoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

private int getRotationAngle(String photoPath) {
    ExifInterface exif = null;
    try {
        exif = new ExifInterface(photoPath);
    } catch (IOException e) {
        e.printStackTrace();
        return 0;
    }
    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
            return 90;
        case ExifInterface.ORIENTATION_ROTATE_180:
            return 180;
        case ExifInterface.ORIENTATION_ROTATE_270:
            return 270;
        default:
            return 0;
    }
}
}