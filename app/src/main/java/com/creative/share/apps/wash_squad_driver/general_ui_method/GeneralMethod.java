package com.creative.share.apps.wash_squad_driver.general_ui_method;

import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("image")
    public static void displayImageHome(ImageView imageView, int image_resource)
    {
        imageView.setImageResource(image_resource);
    }





    @BindingAdapter("date")
    public static void date (TextView textView,long date)
    {
        Log.e("date",date+"_");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));
        textView.setText(m_date);

    }

    @BindingAdapter("rate")
    public static void rate (SimpleRatingBar simpleRatingBar, float rate)
    {
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget(rate)
                .setDuration(1000)
                .setRepeatCount(0)
                .setInterpolator(new LinearInterpolator());
        builder.start();
    }







}
