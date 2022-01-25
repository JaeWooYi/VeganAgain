package com.example.test.dailog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.test.R;

public class MenuDialog extends Dialog {

    private String type;
    private Button btn_check;

    // 생성자 두번째 인수에 type을 넣어서 생성자 안에서 다른 타입에 따라 분리 가능
    public MenuDialog (Context context, String type)
    {
        super(context);

        //다이얼로그 밖의 화면은 흐리게 만들어줌.
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        this.type = type;

//           MainActivity에서 MenuDialog를 생성할때 두번째 인수로 받은 type값에 따라
//           서로 다른 레이아웃을 생성새 4가지 메뉴가 다른 팝업창을 띄우도록 하는 부분.

        switch (type)
        {
            case "flex" :
                setContentView(R.layout.dialog_flex);

                break;

            case "pesc" :
                setContentView(R.layout.dialog_pesc);

                break;

            case "vege" :
                setContentView(R.layout.dialog_vege);

                break;

            case "vegan" :
                setContentView(R.layout.dialog_vegan);
                break;
        }

//        // '확인'버튼 클릭 팝업창 사라짐
//        btn_check = findViewById(R.id.btn_check);
//        btn_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
    }

}
