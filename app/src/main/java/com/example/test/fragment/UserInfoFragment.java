package com.example.test.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.activity.LoginActivity;
import com.example.test.activity.MainActivity;
import com.example.test.activity.MemberInitActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoFragment extends Fragment implements MainActivity.OnBackKeyPressedListener{
    Button logoutButton;  //로그아웃 버튼
    Button profileButton; //프로필 버튼
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String email;

    private static final String Tag = "UserInfoFragment"; ///////////

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
        Toast.makeText(getActivity(),"뒤로가기를 한번 더 누를시 종료 됩니다",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).pushOnBackKeyPressedListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);   ///////////// 밑에 버튼에 view까지 다수정

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(onClickListener);

        profileButton = view.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(onClickListener);
///////////////////////////////////////////
        final ImageView profileImageView = view.findViewById(R.id.profileImageView);
        final TextView nameTextView = view.findViewById(R.id.nameTextView);
        final TextView emailTextView = view.findViewById(R.id.emailTextView);
        final TextView birthDayTextView = view.findViewById(R.id.birthDayTextView);
        final TextView genderTextView = view.findViewById(R.id.genderTextView);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(Tag, "DocumentSnapshot data: " + document.getData());
                            if(document.getData().get("photoUrl") != null){
                                Glide.with(getActivity()).load(document.getData().get("photoUrl")).centerCrop().override(500).into(profileImageView);
                            }
                            email = user.getEmail();
                            emailTextView.setText(email);
                            nameTextView.setText(document.getData().get("name").toString());
                            birthDayTextView.setText(document.getData().get("birth").toString());
                            genderTextView.setText(document.getData().get("gender").toString());
                        } else {
                            Log.d(Tag, "No such document");
                        }
                    }
                } else {
                    Log.d(Tag, "get failed with ", task.getException());
                }
            }
        });
//////////////////////////////// 위주석부터 여기까지 데이터 가져오기
//////////////////////////////// 토스트창을띄워서 다시로그인해야 적용된다고할까 아니면 바로 저장할수있게 하는법을 주성님에게 물어볼까
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(LoginActivity.class);
                    break;
                case R.id.profileButton:
                    //프로필 이동
                    Intent intent2 = new Intent(getActivity(), MemberInitActivity.class);
                    startActivity(intent2);
            }
        }
    };
    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }
}