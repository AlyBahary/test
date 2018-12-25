package com.example.bahary.dawarha.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ErrorModel {
    @SerializedName("name")
    @Expose
    private ArrayList<String> name = null;

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }
}
