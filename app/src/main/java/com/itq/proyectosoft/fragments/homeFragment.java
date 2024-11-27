package com.itq.proyectosoft.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.activities.AcercaDeNosotros;
import com.itq.proyectosoft.activities.HomeActivity;
import com.itq.proyectosoft.activities.ProgresoAdopciones;
import com.itq.proyectosoft.activities.UploadDocumentsActivity;
import com.itq.proyectosoft.adapters.PetsAdapter;
import com.itq.proyectosoft.models.Pets;
import com.itq.proyectosoft.providers.PetsProvider;

import java.util.ArrayList;

public class homeFragment extends Fragment {

    RecyclerView recyclerView_pets2;
    PetsProvider mPetsProvider;
    PetsAdapter mPetsAdapter;
    View mView;
    ImageSlider imageSlider;
    Button btnDocuments;
    Button btnProcesoAdopciones;
    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);// Ahora busca el componente ImageSlider dentro de la vista inflada
        recyclerView_pets2 = mView.findViewById(R.id.recycler_view_pets2);
        mPetsProvider = new PetsProvider();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_pets2.setLayoutManager(gridLayoutManager);

        imageSlider = mView.findViewById(R.id.image_slider);

        btnDocuments = mView.findViewById(R.id.buttonDocu);
        btnProcesoAdopciones = mView.findViewById(R.id.buttonProgress);

        btnProcesoAdopciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProgresoAdopciones.class);
                startActivity(intent);
            }
        });

        btnDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UploadDocumentsActivity.class);
                startActivity(intent);
            }
        });
        // Asegúrate de que el componente ImageSlider se haya encontrado
        if (imageSlider != null) {
            ArrayList<SlideModel> slideModels = new ArrayList<>();
            slideModels.add(new SlideModel(R.drawable.slider1, ScaleTypes.CENTER_CROP));
            slideModels.add(new SlideModel(R.drawable.slider2, ScaleTypes.CENTER_CROP));
            slideModels.add(new SlideModel(R.drawable.slider3, ScaleTypes.CENTER_CROP));
            // Establece la lista de imágenes en el ImageSlider
            imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        } else {
            // Lanza un error o maneja el caso en que el ImageSlider no se encuentra
            throw new RuntimeException("ImageSlider no encontrado en la vista");
        }
        // Retorna la vista inflada para que sea visible en la pantalla
        return mView;
    }
    public void onStart() {
        super.onStart();
        Query query = mPetsProvider.getAll();
        FirestoreRecyclerOptions<Pets> options =
                new FirestoreRecyclerOptions.Builder<Pets>()
                        .setQuery(query, Pets.class)
                        .build();
        mPetsAdapter = new PetsAdapter(options, getContext());
        recyclerView_pets2.setAdapter(mPetsAdapter);
        mPetsAdapter.startListening();
    }
    public void onStop() {
        super.onStop();
        mPetsAdapter.stopListening();
    }
}