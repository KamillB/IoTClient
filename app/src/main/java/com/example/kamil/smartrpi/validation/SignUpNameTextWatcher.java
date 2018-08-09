package com.example.kamil.smartrpi.validation;

import com.example.kamil.smartrpi.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import butterknife.Bind;

public class SignUpNameTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() >= 5){
            SignUpActivity.name_valid = true;
        }
        else{
            SignUpActivity.name_valid = false;
        }
    }
}
