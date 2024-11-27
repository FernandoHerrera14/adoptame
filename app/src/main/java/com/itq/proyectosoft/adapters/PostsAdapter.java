package com.itq.proyectosoft.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.models.Posts;
import com.itq.proyectosoft.providers.UserProvider;
import com.squareup.picasso.Picasso;

public class PostsAdapter extends FirestoreRecyclerAdapter<Posts, PostsAdapter.ViewHolder> {

    Context context;
    UserProvider userProvider;

    public PostsAdapter(FirestoreRecyclerOptions<Posts> options, Context context){
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position, @NonNull Posts post) {
        userProvider.getUser(post.getIdUser()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (!task.isSuccessful() && !document.exists())
                {
                    Toast.makeText(context, "Error con la tarea", Toast.LENGTH_SHORT).show();
                }
                else if(!document.contains("userName") && !document.contains("apellidos"))
                {
                    Toast.makeText(context, "Error en la DB", Toast.LENGTH_SHORT).show();
                }
                else if(post.getImage() == null && post.getImage().isEmpty())
                {
                    Toast.makeText(context, "Error con la consulta del storage", Toast.LENGTH_SHORT).show();
                    holder.imageViewPost.setImageResource(R.drawable.ic_error); // Imagen por defecto
                }
                else
                {
                    String username = document.getString("userName") + " " + document.getString("apellidos");
                    holder.textViewUser.setText(username);
                    holder.textViewDescription.setText(post.getDescription());
                    Picasso.get().load(post.getImage()).into(holder.imageViewPost);
                }
            }
        });

    }



    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post_users, parent, false);
        return new ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDescription;
        TextView textViewUser;
        ImageView imageViewPost;


        public ViewHolder(View view){
            super(view);
            textViewDescription = view.findViewById(R.id.textViewDescription);
            textViewUser = view.findViewById(R.id.textViewUser);
            imageViewPost = view.findViewById(R.id.imageViewPost);
            userProvider = new UserProvider();
        }
    }

}