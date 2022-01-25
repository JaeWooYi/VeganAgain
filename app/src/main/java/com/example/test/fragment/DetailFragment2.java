package com.example.test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.test.FoodInfo;
import com.example.test.R;
import com.example.test.adapter.IngredientAdapter;
import com.example.test.adapter.StepAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class DetailFragment2 extends Fragment {

    private Context context;
    private String title, image;
    private TextView tv_title;
    private ImageView iv_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = container.getContext();

        return inflater.inflate(R.layout.fragment_recipe_detail_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){

            title = bundle.getString("title");
            image = bundle.getString("image");

        }

        tv_title = view.findViewById(R.id.tv_title);
        iv_image = view.findViewById(R.id.iv_image);

        Glide.with(context).load(image).into(iv_image);
        tv_title.setText(title);
    }
}
