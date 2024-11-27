package com.itq.proyectosoft.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.adapters.sliderAdapter;
import com.itq.proyectosoft.models.Pets;
import com.itq.proyectosoft.models.ProcesoAdopcion;
import com.itq.proyectosoft.models.StatusAdopcion;
import com.itq.proyectosoft.models.sliderItem;
import com.itq.proyectosoft.providers.AuthProvider;
import com.itq.proyectosoft.providers.PetsProvider;
import com.itq.proyectosoft.providers.ProcesoAdopcionProvider;
import com.itq.proyectosoft.providers.StatusAdopcionProvider;
import com.itq.proyectosoft.providers.UserProvider;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PetDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    sliderAdapter mSliderAdapter;
    List<sliderItem> mSliderItems = new ArrayList<>();

    TextView txtNombrePet;
    TextView txtCaracterPet;
    TextView txtColorPet;
    TextView txtEdadPet;
    TextView txtHistoriaPet;
    TextView txtSexPet;
    TextView txtTamanioPet;

    String mPetId;

    PetsProvider mPetProvider;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    ProcesoAdopcionProvider mProcesoAdopProvider;
    StatusAdopcionProvider mStatusAdopProvider;

    Button btnAdoptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        txtNombrePet = findViewById(R.id.txtNamePet);
        txtCaracterPet = findViewById(R.id.txtCaracterPet);
        txtColorPet = findViewById(R.id.txtColorPet);
        txtEdadPet = findViewById(R.id.txtEdadPet);
        txtHistoriaPet = findViewById(R.id.txtHistoriaPet);
        txtSexPet = findViewById(R.id.txtSexPet);
        txtTamanioPet = findViewById(R.id.txtTamanioPet);

        mPetProvider = new PetsProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mProcesoAdopProvider = new ProcesoAdopcionProvider();
        mStatusAdopProvider = new StatusAdopcionProvider();

        mSliderView = findViewById(R.id.imageSlider);
        mPetId = getIntent().getStringExtra("id");

        btnAdoptar = findViewById(R.id.btnAdoptarPet);

        btnAdoptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adoptar();
            }
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getImgPet();
    }

    private void adoptar(){
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(today);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Crea una referencia a un nuevo documento con un ID automático
        DocumentReference newDocRef = db.collection("procesoAdopcion").document();
        // Obtén el ID del documento
        String docId = newDocRef.getId();

        ProcesoAdopcion procesoAdopcion = new ProcesoAdopcion();
        procesoAdopcion.setIdProceso(docId);
        procesoAdopcion.setIdAdoptante(mAuthProvider.getYouId());
        procesoAdopcion.setIdPet(mPetId);
        procesoAdopcion.setFechaInicio(formattedDate);
        procesoAdopcion.setFechaFin(null);
        mProcesoAdopProvider.createProcesoAdopcion(procesoAdopcion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Pets pets= new Pets();
                    pets.setId(mPetId);
                    pets.setId_adopcion(docId);
                    mPetProvider.updatePet(pets,procesoAdopcion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(PetDetailActivity.this, "Se cambio el id adopcion en Pets", Toast.LENGTH_SHORT).show();
                                statusAdopcion(docId, formattedDate);
                            }
                            else
                                Toast.makeText(PetDetailActivity.this, "No se cambio el id adopcion en Pets", Toast.LENGTH_SHORT).show();

                        }
                    });
                    Toast.makeText(PetDetailActivity.this, "El proceso de adopcion se inicio", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PetDetailActivity.this, "Algo fallo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void statusAdopcion(String idProc, String date){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Crea una referencia a un nuevo documento con un ID automático
        DocumentReference newDocRef = db.collection("statusAdopcion").document();
        // Obtén el ID del documento
        String docId = newDocRef.getId();
        StatusAdopcion statusAdopcion = new StatusAdopcion();
        statusAdopcion.setIdStatus(docId);
        statusAdopcion.setIdProceso(idProc);
        statusAdopcion.setSolicitudRealizada(true);
        statusAdopcion.setRevisionDocumentos(false);
        statusAdopcion.setEntrevista(false);
        statusAdopcion.setCitaEntrega(false);
        statusAdopcion.setFinProceso(false);
        statusAdopcion.setFechaSolicitud(date);
        statusAdopcion.setFechaRevisionDocumentos(null);
        statusAdopcion.setFechaEntrevista(null);
        statusAdopcion.setFechaCitaEntrega(null);

        mStatusAdopProvider.createStatusAdopcion(statusAdopcion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PetDetailActivity.this, "El status de la adopcion inicio", Toast.LENGTH_SHORT).show();
                    Log.d("statusAd", "El status de la adopcion inicio");
                }else {
                    Toast.makeText(PetDetailActivity.this, "El status de la tuvo ERROR", Toast.LENGTH_SHORT).show();
                    Log.d("statusAd", "El status de la adopcion fallo");
                }
            }
        });

    }

    private void getImgPet(){
        mPetProvider.getPetId(mPetId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("imagen"))
                    {
                        String image1 = documentSnapshot.getString("imagen");
                        sliderItem item = new sliderItem();
                        item.setImgUrl(image1);
                        mSliderItems.add(item);
                        //String image2 = documentSnapshot.getString("imagen2");
                        //String image3 = documentSnapshot.getString("imagen3");
                    }
                    if (documentSnapshot.contains("caracter"))
                        txtCaracterPet.setText(documentSnapshot.getString("caracter"));
                    if (documentSnapshot.contains("edad"))
                        txtEdadPet.setText(documentSnapshot.getString("edad"));
                    if (documentSnapshot.contains("color"))
                        txtColorPet.setText(documentSnapshot.getString("color"));
                    if (documentSnapshot.contains("tamaño"))
                        txtTamanioPet.setText(documentSnapshot.getString("tamaño"));
                    if (documentSnapshot.contains("sexo"))
                        txtSexPet.setText(documentSnapshot.getString("sexo"));
                    if (documentSnapshot.contains("nombre"))
                        txtNombrePet.setText(documentSnapshot.getString("nombre"));
                    if (documentSnapshot.contains("historia"))
                        txtHistoriaPet.setText(documentSnapshot.getString("historia"));

                    mSliderAdapter = new sliderAdapter(PetDetailActivity.this, mSliderItems);
                    mSliderView.setSliderAdapter(mSliderAdapter);
                    mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
                    mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                    mSliderView.setIndicatorSelectedColor(Color.WHITE);
                    mSliderView.setIndicatorUnselectedColor(Color.GRAY);
                    mSliderView.setScrollTimeInSec(3);
                    mSliderView.setAutoCycle(true);
                    mSliderView.startAutoCycle();
                }
            }
        });
    }
}