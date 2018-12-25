package com.example.bahary.dawarha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bahary.dawarha.Models.SignupModel;
import com.example.bahary.dawarha.Models.ErrorModel;
import com.example.bahary.dawarha.Utils.Connectors;
import com.example.bahary.dawarha.Utils.Constants;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    EditText Register_Name, Register_Email, Register_Mobile, Register_pass;
    String Name, Email, Pass, Mobile;
    Button Register_signup;
    TextView sigup_login;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        Register_Name = findViewById(R.id.Register_Name);
        Register_Email = findViewById(R.id.Register_Email);
        Register_Mobile = findViewById(R.id.Register_Mobile);
        Register_pass = findViewById(R.id.Register_pass);
        Register_signup = findViewById(R.id.Register_signup);
        Register_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Register_Name.getText().toString() == null || Register_Name.getText().toString().equals("")) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Please enter your Name", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else if (Register_Email.getText().toString() == null || Register_Email.getText().toString().equals("")) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Please enter your Email", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else if (Register_Mobile.getText().toString() == null || Register_Mobile.getText().toString().equals("")) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Please enter your Mobile", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else if (Register_pass.getText().toString() == null || Register_pass.getText().toString().equals("")) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Please enter your Password", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else {
                    Name = Register_Name.getText().toString();
                    Email = Register_Email.getText().toString();
                    Mobile = Register_Mobile.getText().toString();
                    Pass = Register_pass.getText().toString();
                    signUp(Name, Email, Mobile, Pass);
                    //Sign up Api Call
                }
            }
        });
        sigup_login = findViewById(R.id.sigup_login);
        sigup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void signUp(String name, String email, String mobile, String pass) {
        pd.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connectors.ApiServiceCall.BaseURL)
                .addConverterFactory(GsonConverterFactory
                        .create(new Gson())).build();
        Connectors.ApiServiceCall serviceCall = retrofit.create(Connectors.ApiServiceCall.class);
        serviceCall.mSignup(name + "", email + "", mobile + "", pass + "").enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    SignupModel signupModel = response.body();
                    if (signupModel != null) {
                        if (signupModel.getMessage().equals("failed")) {
                            ErrorModel errorModel = signupModel.getError();
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Please check your Data", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();

                        } else {
                            Hawk.put(Constants.token, signupModel.getApiToken());
                            Hawk.put(Constants.email, signupModel.getEmail());
                            Hawk.put(Constants.name, signupModel.getName());
                            Hawk.put(Constants.phone, signupModel.getPhone());
                            Hawk.put(Constants.photo, signupModel.getPhoto());
                            Hawk.put(Constants.Account_flag, "1");
                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Please Check your internet Connection", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
                pd.dismiss();


            }
        });
    }

}
