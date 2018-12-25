package com.example.bahary.dawarha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bahary.dawarha.Utils.Constants;
import com.example.bahary.dawarha.WelcomeSlidesFragments.Slide1Fragment;
import com.example.bahary.dawarha.WelcomeSlidesFragments.Slide2Fragmet;
import com.example.bahary.dawarha.WelcomeSlidesFragments.Slide3Fragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.orhanobut.hawk.Hawk;

public class WelcomSliderActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hawk.init(this).build();
        if (Hawk.contains(Constants.Account_flag)) {
            if (Hawk.get(Constants.Account_flag).equals("1")) {

                Intent i = new Intent(WelcomSliderActivity.this, HomeActivity.class);
                startActivity(i);
                finish();

            }
        }
        //setContentView(R.layout.activity_welcom_slider);
        Slide1Fragment slide1Fragment = new Slide1Fragment(new Slide1Fragment.OnItemClick() {
            @Override
            public void setOnItemClick() {
                //Toast.makeText(WelcomSliderActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                nextButton.performClick();

            }
        });
        Slide2Fragmet slide2Fragment = new Slide2Fragmet(new Slide2Fragmet.OnItemClick() {
            @Override
            public void setOnItemClick() {
                nextButton.performClick();
            }
        });
        Slide3Fragment slide3Fragment = new Slide3Fragment(new Slide3Fragment.OnItemClick() {
            @Override
            public void setOnItemClick() {
                doneButton.performClick();
                Intent intent = new Intent(WelcomSliderActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addSlide(slide1Fragment);
        addSlide(slide2Fragment);
        addSlide(slide3Fragment);
        showSkipButton(false);
        showPagerIndicator(false);
        showSeparator(false);
        showStatusBar(false);
        setProgressButtonEnabled(false);
        //nextButton.setPressed(true);

    }
}
