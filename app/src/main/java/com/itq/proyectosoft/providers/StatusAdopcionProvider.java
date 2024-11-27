package com.itq.proyectosoft.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itq.proyectosoft.models.ProcesoAdopcion;
import com.itq.proyectosoft.models.StatusAdopcion;

public class StatusAdopcionProvider {
    CollectionReference mCollection;

    public StatusAdopcionProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("statusAdopcion");
    }
    public Task<DocumentSnapshot> getStatusAdopcion(String id){
        return mCollection.document(id).get();
    }
    public Task<Void> createStatusAdopcion(StatusAdopcion statusAdopcion){
        return mCollection.document(statusAdopcion.getIdProceso()).set(statusAdopcion);
    }
}
