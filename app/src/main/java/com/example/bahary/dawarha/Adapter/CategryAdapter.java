package com.example.bahary.dawarha.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.bahary.dawarha.R;


import com.example.bahary.dawarha.Models.CategriesModel;

import java.util.ArrayList;

public class CategryAdapter extends RecyclerView.Adapter<CategryAdapter.ViewHolder> {
    private ArrayList<CategriesModel> categoryModels;
    private Context context;
    private OnItemClick mOnItemClick;

    public CategryAdapter(ArrayList<CategriesModel> categoryModels, Context context, OnItemClick mOnItemClick) {
        this.categoryModels = categoryModels;
        this.context = context;
        this.mOnItemClick = mOnItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catrgry_itme, parent, false);
        return new CategryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategriesModel  N = categoryModels.get(position);
        holder.catg_name.setText(N.getName());

    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView catg_name;

        public ViewHolder(View itemView) {
            super(itemView);
            catg_name = itemView.findViewById(R.id.catg_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClick.setOnItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClick {
        void setOnItemClick(int position);
    }
}
