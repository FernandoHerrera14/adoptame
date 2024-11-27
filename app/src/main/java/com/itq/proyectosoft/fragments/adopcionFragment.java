package com.itq.proyectosoft.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.adapters.PetsAdapter;
import com.itq.proyectosoft.models.Pets;
import com.itq.proyectosoft.providers.PetsProvider;

public class adopcionFragment extends Fragment {


    View mView;
    RecyclerView recyclerView_pets;
    PetsProvider mPetsProvider;
    PetsAdapter mPetsAdapter;
    public adopcionFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_adopcion, container, false);
        recyclerView_pets = mView.findViewById(R.id.recycler_view_pets);
        mPetsProvider = new PetsProvider();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_pets.setLayoutManager(gridLayoutManager);
        return mView;
    }
    @Override
    public void onStart() {
        super.onStart();
        Query query = mPetsProvider.getAll();
        FirestoreRecyclerOptions<Pets> options =
                new FirestoreRecyclerOptions.Builder<Pets>()
                .setQuery(query, Pets.class)
                .build();
        mPetsAdapter = new PetsAdapter(options, getContext());
        recyclerView_pets.setAdapter(mPetsAdapter);
        mPetsAdapter.startListening();
    }
    public void onStop() {
        super.onStop();
        mPetsAdapter.stopListening();
    }
}