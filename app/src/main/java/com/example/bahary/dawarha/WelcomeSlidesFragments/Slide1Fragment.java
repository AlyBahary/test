package com.example.bahary.dawarha.WelcomeSlidesFragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bahary.dawarha.R;
import com.example.bahary.dawarha.WelcomSliderActivity;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Slide1Fragment extends Fragment implements View.OnClickListener {


    private OnItemClick mOnItemClick;
    Button next;
    public Slide1Fragment(OnItemClick mOnItemClick) {
        this.mOnItemClick=mOnItemClick;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_slide1, container, false);
        String[] permissions = {
                 Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.CAMERA};
        String rationale = "Please provide Some permission to use Dawarha easily";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Request");

        Permissions.check(getContext()/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
                getActivity().finish();
            }
        });
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
