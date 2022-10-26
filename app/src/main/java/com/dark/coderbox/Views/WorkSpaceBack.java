package com.dark.coderbox.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dark.coderbox.DarkServices.ThemeMannager.ThemeModule;
import com.dark.coderbox.R;

public class WorkSpaceBack extends LinearLayout {

    public final TypedArray attr;
    public final AttributeSet attrs;
    public LinearLayout WORK_BACK_BG;
    public int ID = 0;
    public boolean IS_EDITING = false;

    @SuppressLint({"ClickableViewAccessibility", "CustomViewStyleable"})

    public WorkSpaceBack(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        inflate(context, R.layout.workspace_back, this);

        attr = context.obtainStyledAttributes(attrs, R.styleable.WorkSpaceLayout_Back);
        WORK_BACK_BG = findViewById(R.id.WORK_BACK_BG);
    }

    public void Add_Items(String tag) {
        ID++;
        WorkSpaceLayout workSpaceLayout = new WorkSpaceLayout(getContext(), attrs);
        workSpaceLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        workSpaceLayout.setTag(tag);
        workSpaceLayout.setGravity(Gravity.CENTER);
        workSpaceLayout.SetText(tag);
        ThemeModule.SetBackData(8, "#FFFFFF", 4, "#FF845D", workSpaceLayout);


        if (IS_EDITING) {

        } else {
//            workSpaceLayout.setOnLongClickListener(v -> {
//                if (IS_EDITING) {
//                    IS_EDITING = false;
//                } else {
//                    if (!IS_EDITING) {
//                        IS_EDITING = true;
//                        ShowMessage("Editing !");
//                    }
//                }
//                return false;
//            });
        }
//        workSpaceLayout.setOnClickListener(v -> {
//            ShowMessage((String) v.getTag());
//            LayoutParams layout_Params = new LayoutParams(Get_Dp_Px(200), Get_Dp_Px(200));
//            layout_Params.setMargins(12, 12, 12, 12);
//            workSpaceLayout.setLayoutParams(layout_Params);
//            workSpaceLayout.SetImageSize(Get_Dp_Px(100), Get_Dp_Px(100));
//            workSpaceLayout.SetTextSize(24);
//        });

        workSpaceLayout.setOnTouchListener((v, event) -> {
            int x = 0;
            int y = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    x = (int) event.getRawX();
                    y = (int) event.getRawY();

                    break;
                case MotionEvent.ACTION_MOVE:

                    int nowX, nowY;
                    nowX = (int) event.getRawX();
                    nowY = (int) event.getRawY();

                    float distX = nowX - x;
                    float distY = nowY - y;
                    ShowMessage(distX + " x" + distY + " y");
                    x = nowX;
                    y = nowY;

                    workSpaceLayout.setX(workSpaceLayout.getX() + distX);
                    workSpaceLayout.setY(workSpaceLayout.getY() + distY);

                    break;
            }
            return true;
        });


        WORK_BACK_BG.addView(workSpaceLayout);
    }

    public void ShowMessage(String txt) {
        //Toast
        Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show();

    }

    public int Get_Dp_Px(int dp) {
        ShowMessage(String.valueOf((int) (dp * getResources().getDisplayMetrics().density)));
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public int Get_Px_Dp(int px) {
        return (int) ((px * 160) / getResources().getDisplayMetrics().density);
    }

}
