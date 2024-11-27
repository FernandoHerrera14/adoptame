package com.itq.proyectosoft.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.itq.proyectosoft.models.Posts;

public class PostProvider {

    CollectionReference mCollection;
    public PostProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<Void> save(Posts post) {
        return mCollection.document().set(post);
    }

    public Query getAll() { //AQUI SE OBTIENEN TODOS LOS POSTS
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getPostByUser(String id){
        return mCollection.whereEqualTo("idUser", id);
    }
}
