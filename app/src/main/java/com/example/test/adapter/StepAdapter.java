package com.example.test.adapter;

import android.content.Context;
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

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.CustomViewHolder> {

    private ArrayList list;
    private Context context;

    public StepAdapter(ArrayList list, Context context) {
        this.context = context;
       this.list = list;

    }

    @NonNull
    @Override
    public StepAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steps, parent, false);
        StepAdapter.CustomViewHolder holder = new StepAdapter.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.CustomViewHolder holder, int position) {

        HashMap hashMap = (HashMap) list.get(position);

        holder.tv_step.setText(String.valueOf(position+1));//1을빼면 스텝다시 0부터시작
        holder.tv_step_bottom.setText(hashMap.get("step").toString());

        // equipment 이미지뷰에 사진 담기
        ArrayList list1 = (ArrayList) hashMap.get("equipment");
        if(list1 != null && list1.size() != 0) {
            HashMap hashMap1 = (HashMap) list1.get(0);             // 리스트에서 첫번째 1개만 리턴
            Glide.with(holder.itemView).load(hashMap1.get("image")).into(holder.iv_equipment);
        }

        // ingredients 이미지뷰에 사진 담기
        ArrayList list2 = (ArrayList) hashMap.get("ingredients");
        if(list2 != null && list2.size() != 0) {
            HashMap hashMap2 = (HashMap) list2.get(0);              // 리스트에서 첫번째 1개만 리턴
            Glide.with(holder.itemView).load(hashMap2.get("image")).into(holder.iv_ingredient);
        }
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (list != null ? list.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_step, tv_step_bottom;
        private ImageView iv_equipment, iv_ingredient;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_step = itemView.findViewById(R.id.tv_step_num);
            tv_step_bottom = itemView.findViewById(R.id.tv_step_bottom);
            iv_ingredient = itemView.findViewById(R.id.iv_ingredient);
            iv_equipment = itemView.findViewById(R.id.iv_equipment);

        }
    }
}
