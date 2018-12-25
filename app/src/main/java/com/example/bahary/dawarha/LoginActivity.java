package com.example.bahary.dawarha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bahary.dawarha.Models.LoginModel;
import com.example.bahary.dawarha.Utils.Connectors;
import com.example.bahary.dawarha.Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText Login_email, login_pass;
    TextView login_sigup;
    Button login_login;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        Login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);
        login_sigup = findViewById(R.id.login_sigup);
        login_sigup.setPaintFlags(login_sigup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login_sigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);

            }
        });
        login_login = findViewById(R.id.login_login);
        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login_email.getText().toString() == null || Login_email.getText().toString().equals("")) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Please enter your email", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();

                } else if (login_pass.getText().toString() == null || login_pass.getText().toString().equals("")) {

                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Please enter your Password", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else {
                    pd.show();
                    logIn(Login_email.getText().toString() + "", login_pass.getText().toString() + "");

                    //here will call login Api

                }

            }
        });
    }

    private void logIn(String email, String pass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connectors.ApiServiceCall.BaseURL)
                .addConverterFactory(GsonConverterFactory
                        .create(new Gson())).build();
        Connectors.ApiServiceCall serviceCall = retrofit.create(Connectors.ApiServiceCall.class);
        serviceCall.mLogin(email, pass).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                pd.dismiss();
                if (!response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    Log.d("TTT", "onResponse: not ");

                    //
                    Gson gson = new GsonBuilder().create();
                    LoginModel mError=new LoginModel();
                    try {
                        mError= gson.fromJson(response.errorBody().string(),LoginModel .class);
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "" +  mError.getError(), Snackbar.LENGTH_LONG)
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                .show();
                    } catch (IOException e) {
                        Log.d("TTT", "on "+e.getMessage());

                    }
                    //


                } else {
                    LoginModel loginModel = response.body();

                    Log.d("TTT", "onResponse: " + response.body() + "--" + response.toString());

                    if (loginModel != null) {

                        Log.d("TTT", "onResponse: " + loginModel.getMessage() + loginModel.getError());

                        if (loginModel.getMessage().equals("failed")) {
                            Log.d("TTT", "onResponse: " + loginModel.getError());
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "" + loginModel.getError(), Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();

                        } else {
                            Log.d("TTTT", "onResponse: " + loginModel.getApiToken());
                            Hawk.put(Constants.token, loginModel.getApiToken());
                            Hawk.put(Constants.email, loginModel.getEmail());
                            Hawk.put(Constants.name, loginModel.getName());
                            Hawk.put(Constants.phone, loginModel.getPhone());
                            Hawk.put(Constants.photo, loginModel.getPhoto());
                            Hawk.put(Constants.Account_flag, "1");
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                pd.dismiss();
//                LoginModel loginModel=t.body();

                Log.d("TTT", "onResponse: " + t.getMessage() + "----" + t);

                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Please Check your internet Connection", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        });
    }
}
