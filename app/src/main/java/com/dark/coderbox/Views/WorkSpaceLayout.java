package com.dark.coderbox.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dark.coderbox.R;

public class WorkSpaceLayout extends LinearLayout {

    private final TypedArray typedArray;
    private final ImageView WORK_IC;
    private final TextView WORK_TXT;
    private final LinearLayout WORK_BG;

    public WorkSpaceLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.work_space_base, this);

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.WorkSpaceLayout);
        WORK_IC = findViewById(R.id.IC_WORK);
        WORK_TXT = findViewById(R.id.TXT_WORK);
        WORK_BG = findViewById(R.id.BG_WORK);

        WORK_IC.setImageResource(typedArray.getResourceId(R.styleable.WorkSpaceLayout_Image, R.color.colorAccent));
        SetText(typedArray.getString(R.styleable.WorkSpaceLayout_Text));
        SetTextSize(typedArray.getInt(R.styleable.WorkSpaceLayout_TextSize, 16));
        WORK_TXT.setTextColor(Color.BLACK);
        SetBackData(12, "#FFFFFFFF");

        WORK_IC.setOnClickListener(v -> {
            ShowMessage("Imageview Clicked");
        });

    }

    public void ShowMessage(String txt) {
        //Toast
        Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show();
    }

    public void SetImageSize(int width, int height) {
        WORK_IC.setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    public String GetText() {
        return typedArray.getString(R.styleable.WorkSpaceLayout_Text);
    }

    public void SetText(String txt) {
        WORK_TXT.setText(txt);
    }

    public void SetBackData(int radius, String color_data) {
        GradientDrawable data = new GradientDrawable();
        data.setCornerRadius(radius);
        data.setColor(Color.parseColor(color_data));
        WORK_BG.setBackground(data);
    }

    public void SetTextSize(int size) {
        WORK_TXT.setTextSize(size);
    }
}
