package com.itq.proyectosoft.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.fragments.communityFragment;
import com.itq.proyectosoft.models.Posts;
import com.itq.proyectosoft.providers.AuthProvider;
import com.itq.proyectosoft.providers.PostProvider;
import com.itq.proyectosoft.providers.UserProvider;
import com.itq.proyectosoft.providers.imageProvider;
import com.itq.proyectosoft.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class postActivity extends AppCompatActivity {

    ImageView imgPost;
    ImageView btnCamara;
    File mImageFile;
    TextInputEditText mTextInputDescription;
    TextView txtUser;
    imageProvider mImageProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    Button btnPost;
    String description = "";

    UserProvider userProvider;
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;
    CircleImageView imageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Instancias
        imgPost = findViewById(R.id.image_post);
        btnCamara = findViewById(R.id.btnCamara);
        btnPost = findViewById(R.id.btn_publish_post);
        mTextInputDescription = findViewById(R.id.post_description);
        txtUser = findViewById(R.id.user_name_post);
        imageProfile = findViewById(R.id.imageProfile);

        userProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mImageProvider = new imageProvider();
        mPostProvider = new PostProvider();
        //FIN DE INSTANCIAS
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publish();
            }
        });

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamara();
            }
        });

        getUser();
    }

    private void getUser()
    {
        userProvider.getUser(mAuthProvider.getYouId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("email") && documentSnapshot.contains("userName") &&
                            documentSnapshot.contains("apellidos") && documentSnapshot.contains("img_profile"))
                    {
                        String nombreCompleto = documentSnapshot.getString("userName") + " " + documentSnapshot.getString("apellidos");
                        String imgProfile = documentSnapshot.getString("img_profile");
                        txtUser.setText(nombreCompleto);
                        if (imgProfile != null){
                            if(!imgProfile.isEmpty())
                                Picasso.get().load(imgProfile).into(imageProfile);
                            else
                                Toast.makeText(postActivity.this, "NO HAY FOTO FE PERfil", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(postActivity.this, "No existe el campo de imagen", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void publish(){
        description = mTextInputDescription.getText().toString();
        if (!description.isEmpty()) {
            if(mImageFile != null)
                saveImage(mImageFile);
            else if (mPhotoFile != null) {
                saveImage(mPhotoFile);
            }
        } else {
            Toast.makeText(this, "Debes ingresar una descripción", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImage(File imageFile)
        {
            mImageProvider.save(postActivity.this, imageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                Posts post = new Posts();
                                post.setImage(url);
                                post.setDescription(description);
                                post.setUserName(mAuthProvider.getUserName());
                                post.setIdUser(mAuthProvider.getYouId());
                                post.setTimestamp(new Date().getTime());
                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> taskPost) {
                                        if (taskPost.isSuccessful()) {
                                            Toast.makeText(postActivity.this, "La información se almacenó correctamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(postActivity.this, "La información no se pudo almacenar", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(postActivity.this, "La imagen no se pudo guardar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
                    mImageFile = FileUtil.from(postActivity.this, result.getData().getData());
                    imgPost.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                Toast.makeText(postActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
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
                Uri photoUri = FileProvider.getUriForFile(postActivity.this,"com.itq.proyectosoft", photoFile);
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
                                    Picasso.get().load(mPhotoPath).into(imgPost);
                                    Log.e("foto", "listo");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("error", "NO "+e);
                                }
                            }
                            else
                                Toast.makeText(postActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
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


}

