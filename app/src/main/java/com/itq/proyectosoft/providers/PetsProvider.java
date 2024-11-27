package com.itq.proyectosoft.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.itq.proyectosoft.models.Pets;
import com.itq.proyectosoft.models.ProcesoAdopcion;
import com.itq.proyectosoft.models.User;

import java.util.HashMap;
import java.util.Map;

public class PetsProvider {

    CollectionReference mCollection;
    public PetsProvider() { mCollection = FirebaseFirestore.getInstance().collection("Pets");}

    public Task<Void> save(Pets pets) {
        return mCollection.document().set(pets);
    }

    public Query getAll() { //AQUI SE OBTIENEN TODOS LOS POSTS
        return mCollection.orderBy("nombre", Query.Direction.DESCENDING);
    }

    public Task<DocumentSnapshot> getPetId(String id){
        return mCollection.document(id).get();
    }

    public Task<Void> updatePet(Pets pets, ProcesoAdopcion a){
        Map<String, Object> map = new HashMap<>();
        map.put("id_adopcion", a.getIdProceso());
        return mCollection.document(pets.getId()).update(map);
    }

}
