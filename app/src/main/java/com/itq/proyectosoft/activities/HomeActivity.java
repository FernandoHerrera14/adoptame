package com.itq.proyectosoft.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.itq.proyectosoft.R;
import com.itq.proyectosoft.fragments.adopcionFragment;
import com.itq.proyectosoft.fragments.communityFragment;
import com.itq.proyectosoft.fragments.homeFragment;
import com.itq.proyectosoft.fragments.profileFragment;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Ventas del menu en la parte inferior
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.itemHome)
                {

                    openFragment(new homeFragment());
                    //Fragmento de Home
                }
                else if(item.getItemId() == R.id.itemAdopciones)
                {
                    openFragment(new adopcionFragment());
                }
                else if (item.getItemId() == R.id.itemComunidad)
                {
                    openFragment(new communityFragment());
                }
                else if (item.getItemId() == R.id.itemPerfil)
                {
                    openFragment(new profileFragment());
                }
                return true;
            }
        });

        openFragment(new homeFragment());

    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}