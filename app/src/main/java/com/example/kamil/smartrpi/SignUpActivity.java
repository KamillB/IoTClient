package com.example.kamil.smartrpi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.smartrpi.models.UserModel;
import com.example.kamil.smartrpi.rest.RestMethods;
import com.example.kamil.smartrpi.rest.RestService;
import com.example.kamil.smartrpi.validation.SignUpMailTextWatcher;
import com.example.kamil.smartrpi.validation.SignUpNameTextWatcher;
import com.example.kamil.smartrpi.validation.SignUpPasswordTextWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.btn_signup) Button signup_btn;
    @Bind(R.id.input_name) EditText name_text;
    @Bind(R.id.input_email) EditText mail_text;
    @Bind(R.id.input_password) EditText password_text;
//    @Bind(R.id.input_password_repeat) EditText password_repeat_text;
    @Bind(R.id.link_login) TextView login_page_redirect;

    public static Boolean name_valid = false,
            mail_valid = false,
            password_valid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        name_text.addTextChangedListener(new SignUpNameTextWatcher());
        mail_text.addTextChangedListener(new SignUpMailTextWatcher());
        password_text.addTextChangedListener(new SignUpPasswordTextWatcher());
    }

    @OnClick(R.id.btn_signup)
    void registerUser() {
        if (name_valid && mail_valid && password_valid){
            UserModel user = new UserModel(
                    name_text.getText().toString(),
                    mail_text.getText().toString(),
                    password_text.getText().toString()
            );

            RestMethods service = RestService.getRetrofitInstance().create(RestMethods.class);
            Call<String> call = service.registerUser(user);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        switch(response.body()){
                            case "Success" :
                                onSuccess();
                                break;
                            case "UsedMail" :
                                onUsedMail();
                                break;
                            case "UsedName" :
                                onUsedName();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this,
                            "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this,
                    "Some of your data format isn't correct.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.link_login)
    void loginRedirect(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void onSuccess(){
        Toast.makeText(SignUpActivity.this,
                "Successfully created new user",
                Toast.LENGTH_SHORT).show();

        loginRedirect();
    }

    private void onUsedMail(){
        Toast.makeText(SignUpActivity.this,
                "This mail is already in use",
                Toast.LENGTH_SHORT).show();
    }

    private void onUsedName(){
        Toast.makeText(SignUpActivity.this,
                "This name is already in use",
                Toast.LENGTH_SHORT).show();
    }
}
