package com.itq.proyectosoft.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.itq.proyectosoft.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserProvider {
    private CollectionReference mCollection;

    public UserProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id) {
            return mCollection.document(id).get();
    }

    public Task<Void> createU(User user){
        return mCollection.document(user.getId()).set(user);
    }

    public Task<Void> updateU(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getUserName());
        map.put("apellidos", user.getApellidos());
        map.put("telefono", user.getTelefono());
        map.put("timestamp", new Date().getTime());
        return mCollection.document(user.getId()).update(map);
    }
    public Task<Void> updateU2(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("telefono", user.getTelefono());
        map.put("img_profile", user.getImgProfile());
        map.put("email", user.getEmail());
        return mCollection.document(user.getId()).update(map);
    }
}
