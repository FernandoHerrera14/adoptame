package com.itq.proyectosoft.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itq.proyectosoft.models.ProcesoAdopcion;
import com.itq.proyectosoft.models.User;

public class ProcesoAdopcionProvider {
    CollectionReference mCollection;

    public ProcesoAdopcionProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("procesoAdopcion");
    }

    public Task<DocumentSnapshot> getProcesoAdopcion(String id){
        return mCollection.document(id).get();
    }

    public Task<Void> createProcesoAdopcion(ProcesoAdopcion procesoAdopcion){
        return mCollection.document(procesoAdopcion.getIdProceso()).set(procesoAdopcion);
    }
}
