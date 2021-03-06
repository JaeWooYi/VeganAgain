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
    private ArrayList<FoodInfo> arrayList;   //////foodlist ????????????, foodinfo??? ????????????
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
        Toast.makeText(getActivity(), "??????????????? ?????? ??? ????????? ?????? ?????????", Toast.LENGTH_SHORT).show();
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
//        recyclerView.setHasFixedSize(true); // ?????????????????? ???????????? ??????
        layoutManager = new LinearLayoutManager(getContext());
        foodRecycler.setLayoutManager(layoutManager);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String acsToken = currentFirebaseUser.getUid();

        arrayList = new ArrayList<>(); // User ????????? ?????? ????????? ????????? (??????????????????)

        firebaseFirestore = FirebaseFirestore.getInstance(); // ?????????????????? ?????????????????? ??????

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

        // ?????? - ????????????
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

                            // favorite??? ???????????????????????? ????????????

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
                                                    // ?????? ??????????????? ????????? ??????
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
                                        //?????????????????? ????????? ?????????
                                        foodListAdapter.setArrayList(arrayList);
                                        //?????????????????? ????????? ???????????????
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
                foodRecycler.setAdapter(foodListAdapter); // ????????????????????? ????????? ??????
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

                                // foodList ???????????? ?????????
                                FoodInfo fl = new FoodInfo();
                                Log.d(Tag, document.getId() + " => " + document.getData().get("title"));
                                // ?????? ??????????????? ????????? ??????
                                fl.setFoodImage(document.getData().get("image").toString());
                                fl.setFoodTitle(document.getData().get("title").toString());
                                fl.setList_step((ArrayList) document.getData().get("steps"));
                                fl.setList_ingredient((ArrayList) document.getData().get("ingredients"));
//                                fl.setFoodTitle(document.getData().get("title").toString());
                                fl.setId((Long) document.getData().get("id"));


                                // ????????? ???????????? ??????
                                arrayList.add(fl);
                            }
                            //?????????????????? ????????? ?????????
                            foodListAdapter.setArrayList(arrayList);
                            //?????????????????? ????????? ???????????????
                            foodListAdapter.notifyDataSetChanged();

                        } else {
                            Log.d(Tag, "Error getting documents: ", task.getException());
                        }
                    }
                });


        foodListAdapter = new foodListAdapter(arrayList, getContext(), getActivity());
        foodRecycler.setAdapter(foodListAdapter); // ????????????????????? ????????? ??????

//////////////////////////////////////////////////////////////////////////////////
        // ????????? ???????????? ?????? ????????? ????????? ??????
        searchedList = new ArrayList<>();

        // ?????? ?????????
        searchView = view.findViewById(R.id.action_search);

        // ???????????? ????????? ??????
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // ??????????????? ???????????? ?????? ?

                String search_word = query; // ?????????

                // ???????????? ???????????? ?????? ???????????? ?????? ??????????????? ?????? ??????
                search_word = search_word.replaceAll("\\p{Digit}|\\p{Space}|\\p{Punct}", "");

                searchedList.clear();

                // ?????? ??????????????? ???????????? ???????????? ???????????? ???????????? searchedList??? ?????? ??????
                for (int i = 0; i < arrayList.size(); i++) {
                    String title = arrayList.get(i).getFoodTitle();
                    title = title.toLowerCase();

                    if (title.contains(search_word)) {
                        searchedList.add(arrayList.get(i));
                    }
                }

                // ????????? ????????? ??????
                foodListAdapter = new foodListAdapter(searchedList, getContext(), getActivity());
                foodRecycler.setAdapter(foodListAdapter); // ????????????????????? ????????? ??????
                foodListAdapter.notifyDataSetChanged();

                return true;
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
//??? ?????? ????????? ???????????? ??????
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        arrayList2 = new ArrayList<>();

        // ?????? - ????????????
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

                                                // ????????? ???????????? ??????
                                                arrayList2.add(foodInfo2);
                                            }
                                            // ????????? ????????? ??????
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // ????????????????????? ????????? ??????
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
                        // Pesceterian ?????? ????????? ????????? ?????????
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

                                                // ????????? ???????????? ??????
                                                arrayList2.add(foodInfo2);
                                            }
                                            // ????????? ????????? ??????
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // ????????????????????? ????????? ??????
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

                                                // ????????? ???????????? ??????
                                                arrayList2.add(foodInfo2);
                                            }
                                            // ????????? ????????? ??????
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // ????????????????????? ????????? ??????
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

                                                // ????????? ???????????? ??????
                                                arrayList2.add(foodInfo2);
                                            }
                                            // ????????? ????????? ??????
                                            foodListAdapter2 = new foodListAdapter2(arrayList2, getContext(), getActivity());
                                            foodRecycler.setAdapter(foodListAdapter2); // ????????????????????? ????????? ??????
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