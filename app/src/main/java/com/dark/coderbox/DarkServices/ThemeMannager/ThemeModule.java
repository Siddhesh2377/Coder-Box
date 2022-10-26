package com.dark.coderbox.DarkServices.ThemeMannager;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

public class ThemeModule extends AppCompatActivity {

    public static void SetBackData(int radius, String color_data, int stroke, String Stroke_Color, View view) {
        GradientDrawable data = new GradientDrawable();
        data.setCornerRadius(radius);
        data.setColor(Color.parseColor(color_data));
        data.setStroke(stroke, Color.parseColor(Stroke_Color));
        view.setBackground(data);
    }

    public static void APPLY_THEME(String theme) {
        if (theme.equals("MK.TH.DARK234")) {
            //THEME -> Dark

        } else {
            if (theme.equals("MK.TH.LIGHT657")) {
                //THEME -> Light

            }
        }
    }

    public static void ANIM_FADEOUT(View v) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        v.startAnimation(fadeIn);
        v.startAnimation(fadeOut);
        fadeIn.setDuration(300);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(300);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(10 + fadeIn.getStartOffset());
    }

    public static void ANIM_FADE_IN(View v) {
        final AlphaAnimation fadeIn = new AlphaAnimation(1.0f, 0.0f);
        final AlphaAnimation fadeOut = new AlphaAnimation(0.0f, 1.0f);
        v.startAnimation(fadeIn);
        v.startAnimation(fadeOut);
        fadeIn.setDuration(300);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(300);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(10 + fadeIn.getStartOffset());
    }

    public static void ColourAnim(String c1, String c2, View v) {
        final String[] data = {""};
        @SuppressLint("RestrictedApi")
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(c1), Color.parseColor(c2));
        colorAnimation.setDuration(1500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                GradientDrawable data = new GradientDrawable();
                data.setCornerRadius(8);
                data.setColor(Color.WHITE);
                data.setStroke(5, (int) animator.getAnimatedValue());
                v.setBackground(data);
            }

        });

        colorAnimation.start();
    }

}
