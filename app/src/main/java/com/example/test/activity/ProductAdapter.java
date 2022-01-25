package com.example.test.activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.PlaceViewHolder> {
    private ArrayList<RecipeVO> Dataset;

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        private TextView NameView;
        private TextView CARView;
        private TextView PROView;
        private TextView FATView;
        private TextView KCALView;

        public PlaceViewHolder(View v) {
            super(v);
            NameView = v.findViewById(R.id.tvName);
            CARView = v.findViewById(R.id.tvCAR);
            PROView = v.findViewById(R.id.tvPRO);
            FATView = v.findViewById(R.id.tvFAT);
            KCALView = v.findViewById(R.id.tvKCAL);
        }
    }

    public ProductAdapter(ArrayList<RecipeVO> Dataset) {
        this.Dataset = Dataset;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_textview, parent, false);
        PlaceViewHolder vh = new PlaceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.NameView.setText(Dataset.get(position).getName());
        holder.CARView.setText(Dataset.get(position).getCAR());
        holder.PROView.setText(Dataset.get(position).getPRO());
        holder.FATView.setText(Dataset.get(position).getFAT());
        holder.KCALView.setText(Dataset.get(position).getKCAL());
    }

    @Override
    public int getItemCount() {
        return Dataset.size();
    }


}