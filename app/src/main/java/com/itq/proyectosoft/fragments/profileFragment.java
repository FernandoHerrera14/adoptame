package com.itq.proyectosoft.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.activities.EditProfileActivity;
import com.itq.proyectosoft.providers.AuthProvider;
import com.itq.proyectosoft.providers.UserProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileFragment extends Fragment {

    ImageButton mLinearLayoutEditProfile;
    View mView;

    TextView textViewUser;
    TextView textViewEmail;
    UserProvider userProvider;
    AuthProvider authProvider;
    CircleImageView imageProfile;
    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mLinearLayoutEditProfile = mView.findViewById(R.id.ll_edit_profile);

        textViewEmail = mView.findViewById(R.id.txtViewCorreoPerfil);
        textViewUser = mView.findViewById(R.id.txtViewUser);
        imageProfile = mView.findViewById(R.id.imageProfile);


        userProvider = new UserProvider();
        authProvider = new AuthProvider();
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProfile();
            }
        });
        getUser();
        return mView;
    }

    private void goToEditProfile(){
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getUser(){
        userProvider.getUser(authProvider.getYouId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("email") && documentSnapshot.contains("userName") &&
                            documentSnapshot.contains("apellidos"))
                    {
                        String email = documentSnapshot.getString("email");
                        String nombreCompleto = documentSnapshot.getString("userName") + " " + documentSnapshot.getString("apellidos");
                        String imgProfile = documentSnapshot.getString("img_profile");
                        textViewEmail.setText(email);
                        textViewUser.setText(nombreCompleto);
                        if (imgProfile != null){
                            if(!imgProfile.isEmpty())
                                Picasso.get().load(imgProfile).into(imageProfile);
                            else
                                Toast.makeText(getActivity(), "NO HAY FOTO FE PERfil", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getActivity(), "No existe el campo de imagen", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}