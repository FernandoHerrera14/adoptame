package com.itq.proyectosoft.activities;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Validacion_registrar {

    private final TextInputEditText mTextInputName;
    private final TextInputEditText mTextInputApellidos;
    private final TextInputEditText mTextInputEmail;
    private final TextInputEditText mTextInputPassword;
    private final TextInputEditText mTextInputConfirmPassword;
    private final TextInputEditText mTextInputTelefono;

    public Validacion_registrar(TextInputEditText mTextInputName, TextInputEditText mTextInputApellidos,
                                TextInputEditText mTextInputEmail, TextInputEditText mTextInputPassword,
                                TextInputEditText mTextInputConfirmPassword, TextInputEditText mTextInputTelefono){

        this.mTextInputName = mTextInputName;
        this.mTextInputApellidos = mTextInputApellidos;
        this.mTextInputEmail = mTextInputEmail;
        this.mTextInputTelefono = mTextInputTelefono;
        this.mTextInputPassword = mTextInputPassword;
        this.mTextInputConfirmPassword = mTextInputConfirmPassword;
    }

    public String getName(){
        return Objects.requireNonNull(mTextInputName.getText()).toString().trim();
    }
    public String getApellidos(){
        return Objects.requireNonNull(mTextInputApellidos.getText()).toString().trim();
    }
    public String getEmail(){
        return Objects.requireNonNull(mTextInputEmail.getText()).toString().trim();
    }
    public String getPhone(){
        return Objects.requireNonNull(mTextInputTelefono.getText()).toString().trim();
    }
    public String getPassword(){
        return Objects.requireNonNull(mTextInputPassword.getText()).toString().trim();
    }
    public String getConfirmPassword(){
        return Objects.requireNonNull(mTextInputConfirmPassword.getText()).toString().trim();
    }
    public boolean isValidData(){
        String name = getName();
        String apell = getApellidos();
        String pass = getPassword();
        String confirmPass = getConfirmPassword();
        String email = getEmail();
        String phone = getPhone();
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(apell)
                && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirmPass)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone);
    }
    public boolean isValidEmail() {
        String email = getEmail();
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public Boolean isValidPassword()
    {
        String pass = getPassword();
        String confirmPass = getConfirmPassword();
        if (Objects.equals(pass, confirmPass)) {
            return true;
        } else {
            return false;
        }
    }
    public Boolean isLengthValidPassword(){
        String pass = getPassword();
        if(pass.length() > 6)
            return true;
        else
            return false;

    }
    public Boolean isLengthValidPhone(){
        String phone = getPhone();
        if(phone.length() == 10){
            Log.d("num", "validacion >0 && < 11 ");
            return true;
        }
        else{
            return false;
        }
    }
}

