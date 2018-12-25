package com.example.bahary.dawarha.WelcomeSlidesFragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bahary.dawarha.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Slide2Fragmet extends Fragment implements View.OnClickListener {


    private OnItemClick mOnItemClick;
    Button next;
    public Slide2Fragmet(OnItemClick mOnItemClick) {
        this.mOnItemClick=mOnItemClick;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_slide2_fragmet, container, false);
        next=view.findViewById(R.id.next);
        next.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        mOnItemClick.setOnItemClick();
    }

    public interface OnItemClick{
        void setOnItemClick();
    }

}
