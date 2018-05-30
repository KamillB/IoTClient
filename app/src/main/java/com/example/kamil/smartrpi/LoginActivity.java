package com.example.kamil.smartrpi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button login_btn, cancel_btn;
    EditText name_text, password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = (Button) findViewById(R.id.login_button);
        cancel_btn = (Button) findViewById(R.id.cancel_button);
        name_text = (EditText) findViewById(R.id.name_text);
        password_text = (EditText) findViewById(R.id.password_text);
    }

    void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
