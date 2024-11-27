package com.itq.proyectosoft.activities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.itq.proyectosoft.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itq.proyectosoft.adapters.FilesAdapter;
import com.itq.proyectosoft.providers.AuthProvider;
import com.itq.proyectosoft.providers.DocProvider;
import com.itq.proyectosoft.providers.UserProvider;
import com.itq.proyectosoft.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadDocumentsActivity extends AppCompatActivity {

    TextView textViewINE;
    TextView textViewDomicilio;
    TextView textViewPhoto;

    LinearLayout llINE;
    LinearLayout llDomicilio;
    LinearLayout llPhoto;

    ImageView imgViewPDFINE;
    ImageView imgViewPDFDomicilio;
    ImageView imgViewPhotoLugar;

    DocProvider mDocProvider;
    UserProvider userProvider;
    AuthProvider mAuthProvider;
    Uri uri;
    Uri uriD;

    File mImageFile;

    Button btnCargarDocumentos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);

        textViewINE = findViewById(R.id.txtNombreArchivoINE);
        textViewDomicilio = findViewById(R.id.txtNombreArchivoDomicilio);
        textViewPhoto = findViewById(R.id.txtNombreFotoLugar);

        llINE = findViewById(R.id.uploadINE);
        llDomicilio = findViewById(R.id.uploadDomicilio);
        llPhoto = findViewById(R.id.uploadFotoLugar);

        imgViewPDFINE = findViewById(R.id.imgViewPdfINE);
        imgViewPDFDomicilio = findViewById(R.id.imgViewPdfDomicilio);
        imgViewPhotoLugar = findViewById(R.id.imgViewPhoto);

        btnCargarDocumentos = findViewById(R.id.btnCargarDocumentos);

        mAuthProvider = new AuthProvider();
        userProvider = new UserProvider();
        mDocProvider = new DocProvider();

        btnCargarDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargar();
            }
        });
        llINE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarPDFINE();
            }
        });
        llDomicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarPDFDomicilio();
            }
        });
        llPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void cargar(){
        userProvider.getUser(mAuthProvider.getYouId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("email") && documentSnapshot.contains("userName") &&
                            documentSnapshot.contains("apellidos") && documentSnapshot.contains("img_profile"))
                    {
                        String idUser = documentSnapshot.getString("id");
                        saveDocuments(mImageFile, uri, uriD, idUser);
                    }
                }
            }
        });

    }

    public void saveDocuments(File photo, Uri ineUri, Uri domUri, String idUser) {
        mDocProvider.saveDocuments(UploadDocumentsActivity.this, photo, ineUri, domUri, idUser)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            mDocProvider.getStorageDoc().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try {
                                        Toast.makeText(UploadDocumentsActivity.this, "Se han guardado los documentos", Toast.LENGTH_SHORT).show();
                                        Log.d("UploadDocuments", "Documentos guardados correctamente: " + uri.toString());
                                    } catch (Exception e) {
                                        Log.e("UploadDocuments", "Error al mostrar Toast: " + e.getMessage(), e);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadDocumentsActivity.this, "Error al obtener la URL de descarga", Toast.LENGTH_SHORT).show();
                                    Log.e("UploadDocuments", "Error al obtener la URL de descarga: " + e.getMessage(), e);
                                }
                            });
                        } else {
                            Toast.makeText(UploadDocumentsActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                            Log.e("UploadDocuments", "Error en la tarea de subida: " + task.getException().getMessage(), task.getException());
                        }
                    }
                });
    }


    private void cargarPDFDomicilio(){
        Intent openD = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openD.addCategory(Intent.CATEGORY_OPENABLE);
        openD.setType("application/pdf");
        launcherD.launch(openD);
    }
    ActivityResultLauncher<Intent> launcherD = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent dataD = result.getData();
                            if (dataD != null) {
                                uriD = dataD.getData();
                                // Aquí obtienes los datos necesarios del archivo PDF
                                // Obtener el nombre del archivo
                                String fileNameD = getFileName(uriD);
                                // Obtener la extensión del archivo
                                String fileExtension = getFileExtension(uriD);
                                loadPDFPreview2(uriD);
                                textViewDomicilio.setText(fileNameD);
                                // Subir el archivo a Firebase Storage
                                //uploadPDFToFirebaseStorage(uri, fileName, fileExtension);
                            }
                        } else {
                            Toast.makeText(UploadDocumentsActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(UploadDocumentsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void cargarPDFINE(){
        Intent openPdf = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openPdf.addCategory(Intent.CATEGORY_OPENABLE);
        openPdf.setType("application/pdf");
        launcher.launch(openPdf);
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                uri = data.getData();
                                // Aquí obtienes los datos necesarios del archivo PDF
                                // Obtener el nombre del archivo
                                String fileName = getFileName(uri);
                                // Obtener la extensión del archivo
                                String fileExtension = getFileExtension(uri);
                                loadPDFPreview(uri);
                                textViewINE.setText(fileName);
                                // Subir el archivo a Firebase Storage
                                //uploadPDFToFirebaseStorage(uri, fileName, fileExtension);
                            }
                        } else {
                            Toast.makeText(UploadDocumentsActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(UploadDocumentsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    // Método para obtener el nombre del archivo desde su URI
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            cursor.close();
        }
        return fileName;
    }

    // Método para obtener la extensión del archivo desde su URI
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void loadPDFPreview(Uri uri) {
        try {
            // Renderiza la primera página del PDF como un Bitmap
            Bitmap pdfBitmap = renderPDF(uri);
            // Muestra el Bitmap en el ImageView
            imgViewPDFINE.setImageBitmap(pdfBitmap);
            Toast.makeText(this, "Exito al cargar la vista previa del PDF", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la vista previa del PDF", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadPDFPreview2(Uri uri) {
        try {
            // Renderiza la primera página del PDF como un Bitmap
            Bitmap pdfBitmap = renderPDF(uri);
            // Muestra el Bitmap en el ImageView
            imgViewPDFDomicilio.setImageBitmap(pdfBitmap);
            Toast.makeText(this, "Exito al cargar la vista previa del PDF", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la vista previa del PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap renderPDF(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        PdfRenderer.Page page = pdfRenderer.openPage(0); // Abre la primera página del PDF
        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY); // Renderiza la página como un bitmap
        page.close();
        pdfRenderer.close();
        return bitmap;
    }

    private void openGallery(){
    Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT);
        openGallery.setType("image/*");
        launcher3.launch(openGallery);
    }
    ActivityResultLauncher<Intent> launcher3 = registerForActivityResult
        (new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            try {
                                mImageFile = FileUtil.from(UploadDocumentsActivity.this, result.getData().getData());
                                imgViewPhotoLugar.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else
                            Toast.makeText(UploadDocumentsActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
}
