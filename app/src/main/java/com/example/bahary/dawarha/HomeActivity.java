package com.example.bahary.dawarha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bahary.dawarha.Adapter.CategryAdapter;
import com.example.bahary.dawarha.Models.CategriesModel;
import com.example.bahary.dawarha.NavigationDrawerActivities.ProfileAcivity;
import com.example.bahary.dawarha.Utils.Connectors;
import com.example.bahary.dawarha.Utils.Constants;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView drawer_icon;
    LinearLayout profile_item;
    ImageView close_drawer,logout;
    CircleImageView imageView;
    RecyclerView Rv;
    TextView nav_name;
    CategryAdapter categryAdapter;
    ArrayList<CategriesModel> categriesModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCatrgries();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        ///
        profile_item = findViewById(R.id.profile_item);
        profile_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(HomeActivity.this, ProfileAcivity.class);
                startActivity(intent);
            }
        });
        ////
        Rv = findViewById(R.id.Rv);
        categriesModels = new ArrayList<>();
        categryAdapter = new CategryAdapter(categriesModels, this, new CategryAdapter.OnItemClick() {
            @Override
            public void setOnItemClick(int position) {

            }
        });
        Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        Rv.setAdapter(categryAdapter);

        ///
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put(Constants.Account_flag,"0");
                Intent i=new Intent(HomeActivity.this,WelcomSliderActivity.class);
                startActivity(i);
                finish();

            }
        });
        /// ///
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText(Hawk.get(Constants.name) + "");
        imageView = findViewById(R.id.imageView1);
        if (!Hawk.get(Constants.photo).equals("")) {
            Log.d("TTTTT", "onCreate: " + Hawk.get(Constants.photo));
            Picasso.get().load(Hawk.get(Constants.photo) + "").fit().into(imageView);
            }
        close_drawer = findViewById(R.id.close_drawer);
        close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        ////
        drawer_icon = findViewById(R.id.drawer_icon);
        drawer_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    drawer.openDrawer(GravityCompat.START);

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this
                , drawer
                , toolbar
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getCatrgries() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connectors.ApiServiceCall.BaseURL)
                .addConverterFactory(GsonConverterFactory
                        .create(new Gson())).build();
        Connectors.ApiServiceCall serviceCall = retrofit.create(Connectors.ApiServiceCall.class);
        serviceCall.mCategries().enqueue(new Callback<ArrayList<CategriesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CategriesModel>> call, Response<ArrayList<CategriesModel>> response) {
                ArrayList<CategriesModel> categriesModel = response.body();
                if (categriesModel != null) {
                    HomeActivity.this.categriesModels.addAll(categriesModel);
                    HomeActivity.this.categryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategriesModel>> call, Throwable t) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Please Check your internet Connection", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        if (!Hawk.get(Constants.photo).equals("")) {
            Log.d("TTTTT", "onCreate: " + Hawk.get(Constants.photo));
            Picasso.get().load(Hawk.get(Constants.photo) + "").fit().into(imageView);
        }
        super.onRestart();
    }
}
