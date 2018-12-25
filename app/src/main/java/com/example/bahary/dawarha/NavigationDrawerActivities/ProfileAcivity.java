package com.example.bahary.dawarha.NavigationDrawerActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.example.bahary.dawarha.Models.LoginModel;
import com.example.bahary.dawarha.Models.SignupModel;
import com.example.bahary.dawarha.R;
import com.example.bahary.dawarha.Utils.Connectors;
import com.example.bahary.dawarha.Utils.Constants;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileAcivity extends AppCompatActivity {

    ImageView arrow;
    ImageView back;
    TextView title;
    LinearLayout Balance,Balance_layout;
    TextView  balance_no,profile_name;
    ImageView edit_image;
        CircleImageView profile_img;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acivity);
        pd=new ProgressDialog(this);
        profileInfo();
        pd.setMessage("Uploading...");
        back=findViewById(R.id.back);
        profile_name=findViewById(R.id.profile_name);
        profile_name.setText(Hawk.get(Constants.name)+"");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profile_img=findViewById(R.id.profile_img);
        if(!Hawk.get(Constants.photo).equals("")){
            Picasso.get().load(Hawk.get(Constants.photo)+"").fit().into(profile_img);
        }
        title=findViewById(R.id.title);
        title.setText("Profile");
        Balance_layout=findViewById(R.id.Balance_layout);
        Balance=findViewById(R.id.Balance);
        arrow=findViewById(R.id.arrow);

        Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Balance_layout.setVisibility(View.VISIBLE);
                //int id = getResources().getIdentifier("com.example.bahary.dawarha:drawable/" + R.drawable.ic_expand_more_black_24dp, null, null);
                Picasso.get().load(R.drawable.ic_expand_more_black_24dp).into(arrow);

                //arrow.setImageResource(id);
                //Picasso.get().load(R.drawable.ic_expand_more_black_24dp).into(arrow);
//                arrow.setImageDrawable(Resources.R.drawable.ic_expand_more_black_24dp);
            }
        });

        balance_no=findViewById(R.id.balance_no);

        edit_image=findViewById(R.id.edit_image);
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(ProfileAcivity.this)
                        .folderMode(true) // folder mode (false by default)
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                        .returnMode(ReturnMode.ALL) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                        .single() // single mode
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Dawarha") // directory name for captured image  ("Camera" folder by default)
                        .start(); // start image picker activity with request code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            // or get a single image only
            com.esafirm.imagepicker.model.Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {

                //profile_img.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
               // profile_img.setImageBitmap(BitmapFactory.decodeFile(Hawk.get("Image_Path")+""));
                UploadphotoService1(image.getPath());
                Hawk.put("Image_Path", image.getPath());
                pd.show();
                pd.setCancelable(false);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void UploadphotoService1(String path) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connectors.ApiServiceCall.BaseURL)
                .addConverterFactory(GsonConverterFactory
                        .create(new Gson())).build();
        //prepare Data to sent
        File file = new File(path);
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), reqFile);
        RequestBody Token = RequestBody.create(MediaType.parse(""), Hawk.get(Constants.token)+"");

        ///
        Connectors.ApiServiceCall serviceCall = retrofit.create(Connectors.ApiServiceCall.class);
        serviceCall.mEditPhoto(Token,body).enqueue(new Callback<SignupModel>() {

            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                pd.dismiss();
                if(response.isSuccessful()){
                    profile_img.setImageBitmap(BitmapFactory.decodeFile(Hawk.get("Image_Path")+""));
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Photo Successfully uploaded", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                    profileInfo();
                }else{
                    Log.d("TTTT", "onResponse: "+response.toString()+"---"+response.body());
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Photo not uploaded", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                }
            }

            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {
                pd.dismiss();
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Please Check your internet Connection", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        });
    }
    public void profileInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connectors.ApiServiceCall.BaseURL)
                .addConverterFactory(GsonConverterFactory
                        .create(new Gson())).build();
        Connectors.ApiServiceCall serviceCall = retrofit.create(Connectors.ApiServiceCall.class);
        serviceCall.mAccountInfo(Hawk.get(Constants.token)+"").enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if(response.isSuccessful()){
                    LoginModel loginModel=response.body();
                    {
                        Hawk.put(Constants.email, loginModel.getEmail());
                        Hawk.put(Constants.name, loginModel.getName());
                        Hawk.put(Constants.phone, loginModel.getPhone());
                        Hawk.put(Constants.photo, loginModel.getPhoto());
                        Hawk.put(Constants.address,loginModel.getAddress());
                        Hawk.put(Constants.balance,loginModel.getBalance());
                        balance_no.setText(loginModel.getBalance());
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

            }
        });
    }

}
