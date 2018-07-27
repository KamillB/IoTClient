package com.example.kamil.smartrpi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.smartrpi.models.UserModel;
import com.example.kamil.smartrpi.rest.RestMethods;
import com.example.kamil.smartrpi.rest.RestService;
import com.example.kamil.smartrpi.validation.LoginMailTextWatcher;
import com.example.kamil.smartrpi.validation.LoginPasswordTextWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.input_email) EditText mail_text;
    @Bind(R.id.input_password) EditText password_text;
    @Bind(R.id.btn_login) Button login_btn;
    @Bind(R.id.link_signup) TextView signup_page_redirect;

    public static Boolean mail_valid = false,
            password_valid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mail_text.addTextChangedListener(new LoginMailTextWatcher());
        password_text.addTextChangedListener(new LoginPasswordTextWatcher());
    }

    @OnClick(R.id.btn_login)
    void loginUser(){
        if (mail_valid && password_valid){
            UserModel user = new UserModel(
                    mail_text.getText().toString(),
                    password_text.getText().toString()
            );

            RestMethods service = RestService.getRetrofitInstance().create(RestMethods.class);
            Call<String> call = service.loginUser(user);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    switch (response.body()){
                        case "WrongPassword" :
                            onWrongPassword();
                            break;
                        case "MailNotFound" :
                            onMailNotFound();
                            break;
                        default :
                            if (response.body().matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")){
                                loginWithUUID(response.body());
                            }
                            else {
                                onWrongUUID();
                            }
                            break;
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,
                            "Something went wrong, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this,
                    "Some of your data format isn't correct.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.link_signup)
    void signUpRedirect() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void onWrongUUID(){
        Toast.makeText(this,
                "Server incorrect UUID response.",
                Toast.LENGTH_LONG).show();
    }

    private void onWrongPassword(){
        Toast.makeText(this,
                "The password you entered is incorrect.",
                Toast.LENGTH_LONG).show();
    }

    private void onMailNotFound(){
        Toast.makeText(this,
                "The mail you entered was not found.",
                Toast.LENGTH_LONG).show();
    }

    private void loginWithUUID(String uuid_key){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("UUID_KEY", uuid_key);
        startActivity(intent);
    }
}
