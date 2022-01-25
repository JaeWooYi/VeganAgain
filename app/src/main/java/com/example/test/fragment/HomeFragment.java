package com.example.test.fragment;

import android.app.Activity;;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.test.R;
import com.example.test.activity.MainActivity;
import com.example.test.dailog.MenuDialog;

public class HomeFragment extends Fragment implements MainActivity.OnBackKeyPressedListener, View.OnClickListener {

    private ImageView iv_1, iv_2, iv_3, iv_4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_1 = view.findViewById(R.id.flex_1);
        iv_2 = view.findViewById(R.id.pesc_2);
        iv_3 = view.findViewById(R.id.vege_3);
        iv_4 = view.findViewById(R.id.vegan_4);

        iv_1.setOnClickListener(this);
        iv_2.setOnClickListener(this);
        iv_3.setOnClickListener(this);
        iv_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        MenuDialog menuDialog;

        // 각각 이미지 뷰를 눌렀을때 팝업창을 띄우는 부분
        switch (view.getId())
        {
            case R.id.flex_1 :
                menuDialog = new MenuDialog(getContext(), "flex");
                menuDialog.setCancelable(true);
                menuDialog.show();
                break;

            case R.id.pesc_2:
                menuDialog = new MenuDialog(getContext(), "pesc");
                menuDialog.setCancelable(true);
                menuDialog.show();
                break;

            case R.id.vege_3:
                menuDialog = new MenuDialog(getContext(), "vege");
                menuDialog.setCancelable(true);
                menuDialog.show();
                break;

            case R.id.vegan_4:
                menuDialog = new MenuDialog(getContext(), "vegan");
                menuDialog.setCancelable(true);
                menuDialog.show();
                break;
        }

    }

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
}