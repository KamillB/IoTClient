package com.example.kamil.smartrpi.validation;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.kamil.smartrpi.SignUpActivity;

public class SignUpMailTextWatcher implements TextWatcher {
    public static final String EMAIL_VERIFICATION = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() >= 7){
            if (s.toString().matches(EMAIL_VERIFICATION)) {
                SignUpActivity.mail_valid = true;
            }
        }
        else {
            SignUpActivity.mail_valid = false;
        }
    }
}
