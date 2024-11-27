package com.itq.proyectosoft.activities;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginHelper {
    private final TextInputEditText mTextInputEmail;
    private final TextInputEditText mTextInputPass;

    public LoginHelper(TextInputEditText mTextInputEmail, TextInputEditText mTextInputPass){
        this.mTextInputEmail = mTextInputEmail;
        this.mTextInputPass = mTextInputPass;
    }

    public String getEmail(){
        return Objects.requireNonNull(mTextInputEmail.getText()).toString().trim();
    }
    public String getPassword() {
        return Objects.requireNonNull(mTextInputPass.getText()).toString().trim();
    }
}
