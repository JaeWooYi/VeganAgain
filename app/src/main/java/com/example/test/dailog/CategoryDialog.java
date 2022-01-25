package com.example.test.dailog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.test.R;

public class CategoryDialog extends Dialog {

   public TextView tv_1, tv_2, tv_3, tv_4;

 public CategoryDialog (Context context)
 {
     super(context);
     //다이얼로그 밖의 화면은 흐리게 만들어줌.
     WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
     layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
     layoutParams.dimAmount = 0.8f;
     getWindow().setAttributes(layoutParams);
     setContentView(R.layout.dialog_category);

     tv_1 = findViewById(R.id.tv_1);
     tv_2 = findViewById(R.id.tv_2);
     tv_3 = findViewById(R.id.tv_3);
     tv_4 = findViewById(R.id.tv_4);

 }
}
