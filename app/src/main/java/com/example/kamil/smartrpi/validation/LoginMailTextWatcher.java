package com.example.kamil.smartrpi.validation;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.kamil.smartrpi.LoginActivity;

public class LoginMailTextWatcher implements TextWatcher {
    public static final String EMAIL_VERIFICATION = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() >= 5 ){
 //           if (s.toString().matches(EMAIL_VERIFICATION)) {
                LoginActivity.mail_valid = true;
//            }
        }
        else{
            LoginActivity.mail_valid = false;
        }
    }
}
