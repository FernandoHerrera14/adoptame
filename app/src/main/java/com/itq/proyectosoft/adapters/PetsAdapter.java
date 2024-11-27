package com.itq.proyectosoft.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.itq.proyectosoft.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.itq.proyectosoft.activities.PetDetailActivity;
import com.itq.proyectosoft.models.Pets;
import com.itq.proyectosoft.providers.PetsProvider;
import com.squareup.picasso.Picasso;
import android.content.Context;

public class PetsAdapter extends FirestoreRecyclerAdapter<Pets, PetsAdapter.ViewHolder> {

    Context context;
    Button btnAdoptar;

    public PetsAdapter(FirestoreRecyclerOptions<Pets> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Pets model) {
        String petId = null;

        //Consultar a Firebase para obtner el ID del animal
        DocumentSnapshot documentPet = getSnapshots().getSnapshot(position);

        if (documentPet.contains("id_adopcion") ){
            if (documentPet.getString("id_adopcion") == null){
                petId = documentPet.getId();
                holder.textViewPetName.setText(model.getNombre());
                holder.textViewPetInfo.setText(model.getSexo()+", "+model.getEdad());
                if(model.getImagen() == null || model.getImagen().isEmpty()){
                    holder.imageViewPet.setImageResource(R.drawable.ic_error);
                }else{
                    Picasso.get().load(model.getImagen()).into(holder.imageViewPet);
                }
                String finalPetId = petId;
                holder.viewHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PetDetailActivity.class);
                        intent.putExtra("id", finalPetId);
                        context.startActivity(intent);
                    }
                });
            }
            else{
                if(model.getImagen() == null || model.getImagen().isEmpty()){
                    holder.imageViewPet.setImageResource(R.drawable.ic_error);
                }else{
                    Picasso.get().load(model.getImagen()).into(holder.imageViewPet);
                }
                holder.textViewPetName.setText("En proceso");
                holder.textViewPetInfo.setText("de adopcion");
                btnAdoptar.setEnabled(false);
            }
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPetName;
        TextView textViewPetInfo;
        ImageView imageViewPet;


        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewPetName = itemView.findViewById(R.id.tvTitlePet);
            textViewPetInfo = itemView.findViewById(R.id.tvInfoPet);
            imageViewPet = itemView.findViewById(R.id.ivImagePet);
            btnAdoptar = itemView.findViewById(R.id.btn_Adoptar);
            viewHolder = view;
        }
    }
}
