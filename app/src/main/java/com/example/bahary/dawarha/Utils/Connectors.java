package com.example.bahary.dawarha.Utils;

import com.example.bahary.dawarha.Models.CategriesModel;
import com.example.bahary.dawarha.Models.LoginModel;
import com.example.bahary.dawarha.Models.SignupModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class Connectors {

    public interface ApiServiceCall {
        public String BaseURL = Constants.mBase_Url;

        @FormUrlEncoded
        @POST(Constants.mSignup)
        Call<SignupModel> mSignup(
                @Field("name") String name
                , @Field("email") String email
                , @Field("phone") String phone
                , @Field("password") String password);


        @FormUrlEncoded
        @POST(Constants.mLogin)
        Call<LoginModel> mLogin(
                @Field("email") String email
                , @Field("password") String password);
        @FormUrlEncoded
        @POST(Constants.mAccountInfo)
        Call<LoginModel> mAccountInfo(
                @Field("api_token") String api_token);

        @GET(Constants.mCategries)
        Call<ArrayList<CategriesModel>> mCategries();

        @Multipart
        @POST(Constants.mEditPhoto)
        Call<SignupModel> mEditPhoto(
                @Part("api_token") RequestBody api_token
                ,@Part MultipartBody.Part photo
        );
    }


}
