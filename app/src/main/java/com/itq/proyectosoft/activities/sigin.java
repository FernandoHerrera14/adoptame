package com.itq.proyectosoft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.models.User;
import com.itq.proyectosoft.providers.AuthProvider;
import com.itq.proyectosoft.providers.UserProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sigin extends AppCompatActivity {


    TextInputEditText mTextInputName;
    TextInputEditText mTextInputApellidos;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;
    TextInputEditText mTextInputTelefono;
    Button mButtonRegistrar;

    AuthProvider mAuthProvider;
    UserProvider mUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);

        mTextInputName = findViewById(R.id.textInputName);
        mTextInputApellidos = findViewById(R.id.textInputApellidos);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mTextInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);
        mButtonRegistrar = findViewById(R.id.btn_registrar);
        mTextInputTelefono = findViewById(R.id.textInputPhone);

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

        final Validacion_registrar validacionRegistrar = new Validacion_registrar(
                mTextInputName, mTextInputApellidos, mTextInputEmail,
                mTextInputPassword, mTextInputConfirmPassword, mTextInputTelefono);

        if(!validacionRegistrar.isValidData())
        {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }
        else if(!validacionRegistrar.isValidEmail())
        {
            Toast.makeText(this, "El corre no es valido", Toast.LENGTH_SHORT).show();
        }
        else if(!validacionRegistrar.isValidPassword())
        {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else if(!validacionRegistrar.isLengthValidPassword())
        {
            Toast.makeText(this, "La contraseña deben contener 7 caracteres o más", Toast.LENGTH_SHORT).show();
        }
        else if(!validacionRegistrar.isLengthValidPhone())
        {
            Toast.makeText(this, "SE REQUIEREN LOS DIEZ DIGITOS DEL NUMERO CELULAR", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String number = validacionRegistrar.getPhone();
            String email = validacionRegistrar.getEmail();
            String password = validacionRegistrar.getPassword();
            String name = validacionRegistrar.getName();
            String apellidos = validacionRegistrar.getApellidos();
            createUser(email, password, name, apellidos, number);
        }

    }
    private void createUser(String email, String password, String name, String apellidos, String number){
        mAuthProvider.register(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String id = mAuthProvider.getYouId();
                if (id == null) {
                    Toast.makeText(sigin.this, "Error al obtener ID del usuario", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setUserName(name);
                user.setApellidos(apellidos);
                user.setTelefono(number);
                user.setTimestamp(new Date().getTime());
                mUserProvider.createU(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(sigin.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(sigin.this, HomeActivity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(sigin.this, "No se pudo alamcenar el usuario", Toast.LENGTH_SHORT).show();
                });
            }
            else
                Toast.makeText(sigin.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
        });


    }
}