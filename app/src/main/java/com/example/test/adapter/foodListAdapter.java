package com.example.test.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.FoodInfo;
import com.example.test.activity.MainActivity;
import com.example.test.fragment.DetailFragment;
import java.util.ArrayList;

public class foodListAdapter extends RecyclerView.Adapter<foodListAdapter.CustomViewHolder> {

    private ArrayList<FoodInfo> arrayList, list_steps, list_ingri;
    private Context context;
    private Activity activity;
//어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
//선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public foodListAdapter(ArrayList<FoodInfo> arrayList, Context context, Activity activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
//실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getFoodImage())
                .into(holder.foodImage);
        holder.foodTitle.setText(arrayList.get(position).getFoodTitle());
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    // 리스트 갱신하는 부분 추가 jae woo 야.....
    public void setArrayList(ArrayList<FoodInfo> arrayList) {
        this.arrayList = arrayList;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
       private ImageView foodImage;
       private TextView foodTitle;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.foodImage = itemView.findViewById(R.id.foodImage);
            GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.roundimageview);
            foodImage.setBackground(drawable);
            foodImage.setClipToOutline(true);
            this.foodTitle = itemView.findViewById(R.id.foodTitle);

            foodTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    DetailFragment detailFragment = new DetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("image", arrayList.get(position).getFoodImage());
                    bundle.putString("title", arrayList.get(position).getFoodTitle());
                    bundle.putSerializable("step", arrayList.get(position).getList_step());
                    bundle.putSerializable("ingredient", arrayList.get(position).getList_ingredient());
                    bundle.putInt("num", position);
                    bundle.putSerializable("info", arrayList.get(position));
                    bundle.putLong("id", arrayList.get(position).getId());
                    detailFragment.setArguments(bundle);

                    ((MainActivity)activity).replaceFragment(detailFragment);

                }
            });
        }
    }
}