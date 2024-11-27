package com.itq.proyectosoft.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.itq.proyectosoft.R;

public class ProgresoAdopciones extends AppCompatActivity {

    private ImageView step1, step2, step3;
    private TextView tvAnimalName;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progreso_adopciones);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        tvAnimalName = findViewById(R.id.tvAnimalName);

        // Obtener el ID del usuario actual
        String userId = auth.getCurrentUser().getUid();

        // Obtener el documento de procesoAdopcion
        db.collection("procesoAdopcion")
                .whereEqualTo("idAdoptante", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String idProceso = document.getString("idProceso");
                        obtenerNombreAnimal(idProceso);
                        obtenerStatusAdopcion(idProceso);
                    } else {
                        Toast.makeText(ProgresoAdopciones.this, "No hay proceso de adopciones", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void obtenerNombreAnimal(String idProceso) {
        // Obtener el documento de Pets que tenga id_adopcion igual a idProceso
        db.collection("Pets")
                .whereEqualTo("id_adopcion", idProceso)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String nombreAnimal = document.getString("nombre");
                        tvAnimalName.setText(nombreAnimal);
                    } else {
                        Toast.makeText(ProgresoAdopciones.this, "Error al obtener el nombre del animal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void obtenerStatusAdopcion(String idProceso) {
        // Obtener el documento de statusAdopcion
        db.collection("statusAdopcion")
                .document(idProceso)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        boolean solicitudRealizada = document.getBoolean("solicitudRealizada");
                        boolean revisionDocumentos = document.getBoolean("revisionDocumentos");
                        boolean entrevista = document.getBoolean("entrevista");

                        // Actualizar la barra de progreso
                        actualizarProgreso(solicitudRealizada, revisionDocumentos, entrevista);
                    } else {
                        Toast.makeText(ProgresoAdopciones.this, "Error al obtener el status de adopción", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void actualizarProgreso(boolean solicitudRealizada, boolean revisionDocumentos, boolean entrevista) {
        if (solicitudRealizada) {
            step1.setImageResource(R.drawable.ic_step_completed);
        }
        if (revisionDocumentos) {
            step2.setImageResource(R.drawable.ic_step_completed);
        }
        if (entrevista) {
            step3.setImageResource(R.drawable.ic_step_completed);
        }
    }
}
