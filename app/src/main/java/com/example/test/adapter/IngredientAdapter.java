package com.example.test.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.R;

import java.util.ArrayList;
import java.util.HashMap;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.CustomViewHolder> {

    private ArrayList list;
  //  private Context context;

    ////////////////////////////////////
    ///////////////////////////////////

    public IngredientAdapter (ArrayList list, Context context) {
  //      this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public IngredientAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        IngredientAdapter.CustomViewHolder holder = new IngredientAdapter.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.CustomViewHolder holder, int position) {

        HashMap hashMap = (HashMap) list.get(position);

        holder.tv_name.setText(hashMap.get("Name").toString());
        holder.tv_metric.setText(hashMap.get("metric").toString());
        holder.tv_us.setText(hashMap.get("us").toString());
        Glide.with(holder.iv_image)
                .load(hashMap.get("image"))
                .into(holder.iv_image);
    }


    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (list != null ? list.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_metric, tv_us;
        private ImageView iv_image;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_image);
            tv_name= itemView.findViewById(R.id.tv_name);
            tv_metric = itemView.findViewById(R.id.tv_metric);
            tv_us = itemView.findViewById(R.id.tv_us);

        }
    }
}
