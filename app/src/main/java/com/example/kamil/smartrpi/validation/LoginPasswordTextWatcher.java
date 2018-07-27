package com.example.kamil.smartrpi.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.kamil.smartrpi.LoginActivity;

public class LoginPasswordTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() >= 8){
            LoginActivity.password_valid = true;
        }
        else{
            LoginActivity.password_valid = false;
        }
    }
}
