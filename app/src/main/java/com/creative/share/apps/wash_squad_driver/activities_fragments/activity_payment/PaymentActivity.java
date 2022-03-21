package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_payment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_step2;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityPayemntBinding;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityWork1Step1Binding;
import com.creative.share.apps.wash_squad_driver.databinding.DialogSelectImageBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityPayemntBinding binding;

    private String lang;
    private Order_Model.Data data;
    private int payment_method;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, "en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payemnt);
        initView();
    }

    private void initView() {
        data = (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        binding.setBackListener(this);
        binding.rb1.setOnClickListener(view ->
        {
            payment_method = 1;


        });
        binding.rb2.setOnClickListener(view ->
        {
            payment_method = 1;


        });
        binding.rb3.setOnClickListener(view -> {
            payment_method = 2;
        });
        binding.rb4.setOnClickListener(view -> {
            payment_method = 3;
        });
binding.btnStep2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(payment_method==0){
            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.ch_payment), Toast.LENGTH_SHORT).show();
        }
        else{
            step2("");
        }
    }
});
    }
    private void step2(String feedback) {
        final Dialog dialog = Common.createProgressDialog(PaymentActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {


            Api.getService(lang, Tags.base_url)
                    .Step2(data.getId() + "", (Calendar.getInstance().getTimeInMillis() / 1000) + "", feedback,payment_method+"").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(PaymentActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());

                        finish();
                    } else {
                        try {

                            Toast.makeText(PaymentActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.code() + "" + response.errorBody().string() + response.raw() + response.body() + response.headers());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(PaymentActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void back() {
        finish();
    }
}
