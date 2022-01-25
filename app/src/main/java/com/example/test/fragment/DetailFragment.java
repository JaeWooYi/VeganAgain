package com.example.test.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.FoodInfo;
import com.example.test.R;
import com.example.test.activity.MainActivity;
import com.example.test.adapter.IngredientAdapter;
import com.example.test.adapter.StepAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class DetailFragment extends Fragment implements MainActivity.OnBackKeyPressedListener {

    private TextToSpeech tts;
    private String title, image, favorite;
    private ArrayList list_steps, list_ingredient;
    private TextView tv_title;
    private ImageView iv_image;
    private Button ttsB, btn_favorite;
    private Context context;
    private RecyclerView rv_step, rv_ingredient;
    private StepAdapter stepAdapter;
    private RecyclerView.LayoutManager layoutManager, layoutManager1;
    private IngredientAdapter ingredientAdapter;
    private ImageView iv_stop;
    private FoodInfo foodInfo;
    private FirebaseFirestore firebaseFirestore;
    private Long id;
    private boolean isFavoriteChecked = false;
    private FirebaseUser user;
    private String uid;
    private String deleteFavorite;

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
        Toast.makeText(getActivity(), "뒤로가기를 한번 더 누를시 종료 됩니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).pushOnBackKeyPressedListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        setHasOptionsMenu(true);
        context = container.getContext();
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager1 = new LinearLayoutManager(getContext());
        tts = new TextToSpeech(requireContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                    tts.setSpeechRate(0.8f);
                }
            }
        });
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        firebaseFirestore = FirebaseFirestore.getInstance(); // 파이어베이스 데이터베이스 연동
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String acsToken = currentFirebaseUser.getUid();


        if (bundle != null) {

            title = bundle.getString("title");
            list_steps = (ArrayList) bundle.getSerializable("step");
            list_ingredient = (ArrayList) bundle.getSerializable("ingredient");
            image = bundle.getString("image");
            foodInfo = (FoodInfo) bundle.getSerializable("info");
            id = bundle.getLong("id");
        }
        firebaseFirestore.collection("users").document(acsToken).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map dataMap = documentSnapshot.getData();
                    favorite = String.valueOf(dataMap.get("favorite"));

                    if (!(favorite == null)) {
                        if (favorite.contains(id.toString())) {
                            isFavoriteChecked = true;
                            btn_favorite.setBackground(getResources().getDrawable(R.drawable.rounded_background_checked));
                        }
                    }
                }
            }
        });

        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(title);

        iv_image = view.findViewById(R.id.iv_image_top);
        Glide.with(context).load(image).into(iv_image);

        ttsB = view.findViewById(R.id.ttsB);
        // ingredient 리사이클러뷰
        rv_ingredient = view.findViewById(R.id.rv_ingredient);
        rv_ingredient.setLayoutManager(layoutManager1);

        ingredientAdapter = new IngredientAdapter(list_ingredient, context);
        rv_ingredient.setAdapter(ingredientAdapter);
        ingredientAdapter.notifyDataSetChanged();


        // steps 리사이클러뷰
        rv_step = view.findViewById(R.id.rv_step);
        rv_step.setLayoutManager(layoutManager);

        stepAdapter = new StepAdapter(list_steps, context);
        rv_step.setAdapter(stepAdapter);
        stepAdapter.notifyDataSetChanged();

        ttsB.setOnClickListener(v -> {
            for (Object step : list_steps) {
                Map stepM = (Map) step;
                tts.speak(stepM.get("step").toString(), TextToSpeech.QUEUE_ADD, null);
            }
        });


//       즐겨찾기 버튼 (Like)
        btn_favorite = view.findViewById(R.id.btn_favorite);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd");
                String nowYear = simpleDateFormat.format(mDate);
                // 처음 누를때 ( 즐겨찾기에 추가 )
                if (!isFavoriteChecked) {
                    btn_favorite.setBackground(getResources().getDrawable(R.drawable.rounded_background_checked));

                    firebaseFirestore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        //로그인은 user에 ui가 등록된 계정으로 해야한다 ..
                        // 왜 시바 내걸로는 안돼는걸까
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                isFavoriteChecked = true;

                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map dataMap = documentSnapshot.getData();
                                favorite = String.valueOf(dataMap.get("favorite"));

                                if (favorite == null) {

                                    firebaseFirestore.collection("users").document(acsToken).update("favorite", id);
                                } else {
                                    StringBuffer stringBuffer = new StringBuffer(favorite);
                                    String id2 = "@" + id + "#" + nowYear;
                                    stringBuffer.append(id2);
                                    firebaseFirestore.collection("users").document(acsToken).update("favorite", stringBuffer.toString());
                                }
                            }
                        }
                    });
                }

                // 다시 누를때 (즐겨찾기 버튼 해제)
                else {
                    isFavoriteChecked = false;

                    btn_favorite.setBackground(getResources().getDrawable(R.drawable.rounded_background));

                    firebaseFirestore.collection("users").document(acsToken).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
//                                    favorite 변수에는 즐겨찾기 버튼을 누르기 전의 데이터가 들어가있다.
//                                    즐찾버튼을 한번 더 눌러 즐겨찾기를 해제할때에는
//                                    즐찾버튼을 누르기 전의 데이터가 들어가있는 favorite 변수를 파이어스토어에 전달함으로써
//                                    즐찾기 해제 기능을 구현.... 문제가 있지만 나만 알고있도록 하자....
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map dataMap = documentSnapshot.getData();
                                favorite = String.valueOf(dataMap.get("favorite"));
                                String[] favoriteSpilt = favorite.split("@");
                                for (int i = 0; i < favoriteSpilt.length; i++) {
                                    if (favoriteSpilt[i].contains(id.toString())) {
                                        deleteFavorite = favorite.replace("@" + favoriteSpilt[i], "");
                                    }
                                }
                                firebaseFirestore.collection("users").document(acsToken).update("favorite", deleteFavorite);
                                Toast.makeText(getActivity(), "즐겨찾기가 해제 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        // 일시정지
        iv_stop = view.findViewById(R.id.iv_stop);
        iv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.stop();
            }
        });

    }

}
