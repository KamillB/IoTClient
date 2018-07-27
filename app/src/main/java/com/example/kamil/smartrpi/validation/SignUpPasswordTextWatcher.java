package com.example.kamil.smartrpi.validation;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.kamil.smartrpi.SignUpActivity;

public class SignUpPasswordTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() >= 8){
            SignUpActivity.password_valid = true;
        }
        else{
            SignUpActivity.password_valid = false;
        }
    }
}
