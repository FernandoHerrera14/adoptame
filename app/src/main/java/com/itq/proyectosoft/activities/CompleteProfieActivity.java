package com.itq.proyectosoft.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.models.User;
import com.itq.proyectosoft.providers.AuthProvider;
import com.itq.proyectosoft.providers.UserProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CompleteProfieActivity extends AppCompatActivity {

    TextInputEditText mTextInputName;
    TextInputEditText mTextInputApellidos;
    TextInputEditText mTextInputPhone;
    Button mButtonRegistrar;
    AuthProvider mAuthProvider;

    UserProvider mUserProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profie);

        mTextInputName = findViewById(R.id.textInputName);
        mTextInputApellidos = findViewById(R.id.textInputApellidos);
        mButtonRegistrar = findViewById(R.id.btn_registrar);
        mTextInputPhone = findViewById(R.id.textInputPhone);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mButtonRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                registrar();
            }
        });

    }

    private void registrar(){

        String name = mTextInputName.getText().toString();
        String apell = mTextInputApellidos.getText().toString();
        String phone = mTextInputPhone.getText().toString();
        if(name.isEmpty() && apell.isEmpty() && phone.isEmpty())
        {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }
        else if(phone.length() != 10)
        {
            Toast.makeText(this, "El número de teléfono debe tener 10 dígitos", Toast.LENGTH_SHORT).show();
        }
        else{
            updateUser(name, apell, phone);
        }
    }
    private void updateUser(String name, String apellidos, String phone){
        String id = mAuthProvider.getYouId();
        User user = new User();
        user.setId(id);
        user.setUserName(name);
        user.setApellidos(apellidos);
        user.setTelefono(phone);
        user.setTimestamp(new Date().getTime());
        mUserProvider.updateU(user).addOnCompleteListener((task) -> {
            if(task.isSuccessful())
            {
                Intent intent = new Intent(CompleteProfieActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(CompleteProfieActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(CompleteProfieActivity.this, "Error en el registro", Toast.LENGTH_LONG).show();
        });


    }
}