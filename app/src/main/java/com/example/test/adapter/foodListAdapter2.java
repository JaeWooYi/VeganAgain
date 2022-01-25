package com.example.test.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.FoodInfo;
import com.example.test.FoodInfo2;
import com.example.test.R;
import com.example.test.activity.MainActivity;
import com.example.test.fragment.DetailFragment;
import com.example.test.fragment.DetailFragment2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class foodListAdapter2 extends RecyclerView.Adapter<foodListAdapter2.CustomViewHolder> {

    private ArrayList<FoodInfo2> arrayList;
    private Context context;
    private Activity activity;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;



    public foodListAdapter2(ArrayList<FoodInfo2> arrayList, Context context, Activity activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
//실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만듬? 이해를못하지만 걍 씀
    public foodListAdapter2.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        foodListAdapter2.CustomViewHolder holder = new foodListAdapter2.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull foodListAdapter2.CustomViewHolder holder, int position) {
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

//    public void setArrayList(ArrayList<FoodInfo> arrayList) {
//        this.arrayList = arrayList;
//    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodImage;
        private TextView foodTitle;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.foodImage = itemView.findViewById(R.id.foodImage);
            this.foodTitle = itemView.findViewById(R.id.foodTitle);

            foodTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    documentReference = firebaseFirestore.collection("recipe").document("1095849");
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                // foodList 인스턴스 초기화

                                DetailFragment detailFragment = new DetailFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("image", document.getData().get("image").toString());
                                bundle.putString("title", document.getData().get("title").toString());
                                bundle.putSerializable("step", (ArrayList) document.getData().get("steps"));
                                bundle.putSerializable("ingredient", (ArrayList) document.getData().get("ingredients"));
//                                bundle.putInt("num", position);
//                                bundle.putSerializable("info", arrayList.get(position));
                                bundle.putLong("id", (Long) document.getData().get("id"));
                                detailFragment.setArguments(bundle);

                                ((MainActivity)activity).replaceFragment(detailFragment);

                            } else {
                                Log.d("Tag", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
            });
        }
    }
}
