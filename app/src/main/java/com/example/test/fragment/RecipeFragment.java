package com.example.test.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.test.FoodInfo2;
import com.example.test.R;
import com.example.test.activity.MainActivity;
import com.example.test.activity.ProductAdapter;
import com.example.test.activity.RecipeActivity;
import com.example.test.adapter.foodListAdapter;
import com.example.test.FoodInfo;
import com.example.test.adapter.foodListAdapter2;
import com.example.test.dailog.CategoryDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RecipeFragment extends Fragment implements MainActivity.OnBackKeyPressedListener {

    private RecyclerView foodRecycler;
    private foodListAdapter foodListAdapter;
    private com.example.test.adapter.foodListAdapter2 foodListAdapter2;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FoodInfo> arrayList;   //////foodlist 이름변경, foodinfo로 이름변경
    private ArrayList<FoodInfo> searchedList;
    private ArrayList<FoodInfo2> arrayList2;
    private ArrayList listFromFavorite;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private static final String Tag = "RecipeFragment";
    private View view;
    private ProductAdapter productAdapter;
    ////////////////////////////////
    private Context context;
    private ArrayList list_steps, list_ingri;
    private EditText editText;

    private SearchView searchView;
    ////////////////////////////

    private DrawerLayout drawerLayout;
    private View viewDrawer;
    private ImageView iv_menu;
    private LinearLayout category, favorite, voice_rec;

    public RecipeFragment() {
    }

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return;
    }
///////////////////////////////////// 614

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
        view = inflater.inflate(R.layout.fragment_recipe, container, false);
        foodRecycler = (RecyclerView) view.findViewById(R.id.foodRecycler);     //////////// 610
//        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getContext());
        foodRecycler.setLayoutManager(layoutManager);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String acsToken = currentFirebaseUser.getUid();

        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        firebaseFirestore = FirebaseFirestore.getInstance(); // 파이어베이스 데이터베이스 연동

        list_ingri = new ArrayList();       ///////////////
        list_steps = new ArrayList();       ////////////// 610

        viewDrawer = view.findViewById(R.id.drawer);
        iv_menu = view.findViewById(R.id.iv_menu);

        drawerLayout = view.findViewById(R.id.drawer_layout);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(viewDrawer);
            }
        });

        // 메뉴 - 즐겨찾기
        favorite = view.findViewById(R.id.favorite);
        listFromFavorite = new ArrayList<>();
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("users").document(acsToken).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            // favorite에 저장되어있는걸들 가져오기

                            String value = documentSnapshot.getData().get("favorite").toString();

                            StringTokenizer stringTokenizer = new StringTokenizer(value, "@");

                            listFromFavorite.clear();
                            while (stringTokenizer.hasMoreTokens()) {
                                String[] valueSplit = stringTokenizer.nextToken().toString().split("#");

                                listFromFavorite.add(valueSplit[0]);
                            }
                            firebaseFirestore.collection("recipe").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        arrayList.clear();
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            for (int i = 0; i < listFromFavorite.size(); i++) {
                                                if (document.getId().equals(listFromFavorite.get(i))) {
                                                    FoodInfo fl = new FoodInfo();
                                                    Log.d(Tag, document.getId() + " => " + document.getData().get("title"));
                                                    // 해당 인스턴스에 데이터 넣기
                                                    fl.setFoodImage(document.getData().get("image").toString());
                                                    fl.setFoodTitle(document.getData().get("title").toString());
                                                    fl.setList_step((ArrayList) document.getData().get("steps"));
                                                    fl.setList_ingredient((ArrayList) document.getData().get("ingredients"));
                                                    fl.setFoodTitle(document.getData().get("title").toString());
                                                    fl.setId((Long) document.getData().get("id"));

                                                    arrayList.add(fl);
                                                }
                                            }
                                        }
                                        //리사이클러뷰 리스트 재세팅
                                        foodListAdapter.setArrayList(arrayList);
                                        //리사이클러뷰 리스트 재업데이트
                                        foodListAdapter.notifyDataSetChanged();

                                    } else {
                                        Log.d(Tag, "Error getting documents: ", task.getException());
                                    }
                                }
                            });


                        } else {
                            Log.d(Tag, "Error getting documents: ", task.getException());
                        }

                    }
                });

                foodListAdapter = new foodListAdapter(arrayList, getContext(), getActivity());
                foodRecycler.setAdapter(foodListAdapter); // 리사이클러뷰에 어댑터 연결
                drawerLayout.closeDrawer(viewDrawer);
            }
        });

        firebaseFirestore.collection("recipe")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ///////////////////////////////////////

                            for (QueryDocumentSnapshot document : task.getResult()) {               ///////////////////////////////////////

                                // foodList 인스턴스 초기화
                                FoodInfo fl = new FoodInfo();
                                Log.d(Tag, document.getId() + " => " + document.getData().get("title"));
                                // 해당 인스턴스에 데이터 넣기
                                fl.setFoodImage(document.getData().get("image").toString());
                                fl.setFoodTitle(document.getData().get("title").toString());
                                fl.setList_step((ArrayList) document.getData().get("steps"));
                                fl.setList_ingredient((ArrayList) document.getData().get("ingredients"));
//                                fl.setFoodTitle(document.getData().get("title").toString());
                                fl.setId((Long) document.getData().get("id"));


                                // 레시피 리스트에 추가
                                arrayList.add(fl);
                            }
                            //리사이클러뷰 리스트 재세팅
                            foodListAdapter.setArrayList(arrayList);
                            //리사이클러뷰 리스트 재업데이트
                            foodListAdapter.notifyDataSetChanged();

                        } else {
                            Log.d(Tag, "Error getting documents: ", task.getException());
                        }
                    }
                });


        foodListAdapter = new foodListAdapter(arrayList, getContext(), getActivity());
        foodRecycler.setAdapter(foodListAdapter); // 리사이클러뷰에 어댑터 연결

//////////////////////////////////////////////////////////////////////////////////
        // 검색된 리스트를 받는 새로운 리스트 객체
        searchedList = new ArrayList<>();

        // 상단 서치뷰
        searchView = view.findViewById(R.id.action_search);

        // 서치뷰에 리스터 연결
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // 검색했을때 일어나는 부분 ?

                String search_word = query; // 검색어

                // 공백이나 띄어쓰기 등을 영향받지 않고 검색되도록 하는 부분
                search_word = search_word.replaceAll("\\p{Digit}|\\p{Space}|\\p{Punct}", "");

                searchedList.clear();

                // 모든 리스트에서 검색어에 해당하는 리스트만 검색해서 searchedList에 담는 부분
                for (int i = 0; i < arrayList.size(); i++) {
                    String title = arrayList.get(i).getFoodTitle();
                    title = title.toLowerCase();

                    if (title.contains(search_word)) {
                        searchedList.add(arrayList.get(i));
                    }
                }

                // 새로운 어댑터 생성
                foodListAdapter = new foodListAdapter(searchedList, getContext(), getActivity());
                foodRecycler.setAdapter(foodListAdapter); // 리사이클러뷰에 어댑터 연결
                foodListAdapter.notifyDataSetChanged();

                return true;
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
//걍 존나 어려움 왜되는지 모름
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        arrayList2 = new ArrayList<>();

        // 메뉴 - 카테고리
        category = view.findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CategoryDialog categoryDialog = new CategoryDialog(context);
                categoryDialog.setCancelable(true);
                categoryDialog.tv_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseFirestore.collection("flexitarian")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {


                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                FoodInfo2 foodInfo2 = new FoodInfo2();
                                                foodInfo2.setFoodImage(document.getData().get("image").toString());
                                                foodInfo2.setFoodTitle(document.getData().get("title").toString());
                                                foodInfo2.setId((Long) document.getData().get("id"));

                                                // 레시피 리스트에 추가
                                                arrayList2.add(foodInfo2);
                                            }
                                            // 새로운 어댑터 생성
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // 리사이클러뷰에 어댑터 연결
                                            foodListAdapter.notifyDataSetChanged();
                                            categoryDialog.dismiss();
                                            drawerLayout.closeDrawer(viewDrawer);

                                        } else {
                                            Log.d(Tag, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                    }
                });


                categoryDialog.tv_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Vegetarian  sdfsdfdsagasrgasrgsrg
                        // Pesceterian 이거 누를시 염병할 이게뜸
                        firebaseFirestore.collection("Pescetarian")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                FoodInfo2 foodInfo2 = new FoodInfo2();
                                                foodInfo2.setFoodImage(document.getData().get("image").toString());
                                                foodInfo2.setFoodTitle(document.getData().get("title").toString());
                                                foodInfo2.setId((Long) document.getData().get("id"));

                                                // 레시피 리스트에 추가
                                                arrayList2.add(foodInfo2);
                                            }
                                            // 새로운 어댑터 생성
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // 리사이클러뷰에 어댑터 연결
                                            foodListAdapter.notifyDataSetChanged();
                                            categoryDialog.dismiss();
                                            drawerLayout.closeDrawer(viewDrawer);

                                        } else {
                                            Log.d(Tag, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                });


                categoryDialog.tv_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ////dhegththet
                        firebaseFirestore.collection("Vegetarian")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                FoodInfo2 foodInfo2 = new FoodInfo2();
                                                foodInfo2.setFoodImage(document.getData().get("image").toString());
                                                foodInfo2.setFoodTitle(document.getData().get("title").toString());
                                                foodInfo2.setId((Long) document.getData().get("id"));

                                                // 레시피 리스트에 추가
                                                arrayList2.add(foodInfo2);
                                            }
                                            // 새로운 어댑터 생성
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // 리사이클러뷰에 어댑터 연결
                                            foodListAdapter.notifyDataSetChanged();
                                            categoryDialog.dismiss();
                                            drawerLayout.closeDrawer(viewDrawer);

                                        } else {
                                            Log.d(Tag, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                });

                categoryDialog.tv_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        firebaseFirestore.collection("vegan")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {


                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                FoodInfo2 foodInfo2 = new FoodInfo2();
                                                foodInfo2.setFoodImage(document.getData().get("image").toString());
                                                foodInfo2.setFoodTitle(document.getData().get("title").toString());
                                                foodInfo2.setId((Long) document.getData().get("id"));

                                                // 레시피 리스트에 추가
                                                arrayList2.add(foodInfo2);
                                            }
                                            // 새로운 어댑터 생성
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // 리사이클러뷰에 어댑터 연결
                                            foodListAdapter.notifyDataSetChanged();
                                            categoryDialog.dismiss();
                                            drawerLayout.closeDrawer(viewDrawer);

                                        } else {
                                            Log.d(Tag, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                    }
                });

                categoryDialog.show();
            }
        });

        voice_rec = view.findViewById(R.id.voice_recognition);
        voice_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }
}