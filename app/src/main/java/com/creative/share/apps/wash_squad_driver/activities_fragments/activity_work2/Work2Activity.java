package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2;

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
import android.text.TextUtils;
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
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_step4;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityWork2Binding;
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

public class Work2Activity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityWork2Binding binding;

    private String lang;

private Order_Model.Data data;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, "en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work2);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        data= (Order_Model.Data) getIntent().getExtras().getSerializable("detials");


        binding.setBackListener(this);





        binding.btnDone.setOnClickListener(view -> {
            String feedback=binding.edtFeedback.getText().toString();
            if (!TextUtils.isEmpty(feedback))
            {
                binding.edtFeedback.setError(null);

               step2(feedback);
            }else
            {

                if(TextUtils.isEmpty(feedback)){
                    binding.edtFeedback.setError(getResources().getString(R.string.field_req));
                }
            }



        });


    }
    private void step2(String feedback) {
        final Dialog dialog = Common.createProgressDialog(Work2Activity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {




        Api.getService(lang, Tags.base_url)
                .Step2(data.getId()+"",(Calendar.getInstance().getTimeInMillis()/1000)+"",feedback).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                    Toast.makeText(Work2Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                    //  adsActivity.finish(response.body().getId_advertisement());
                    Intent intent = new Intent(Work2Activity.this, HomeActivity.class);
                    intent.putExtra("detials",data);

                    startActivityForResult(intent, 1003);
                    finish();
                } else {
                    try {

                        Toast.makeText(Work2Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        Log.e("Error", response.code() + "" + response.errorBody() + response.raw() + response.body() + response.headers());
                    }catch (Exception e){


                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                try {
                    Toast.makeText(Work2Activity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
                catch (Exception e){

                }
            }
        });}catch (Exception e){
            dialog.dismiss();
        }
    }






    @Override
    public void back() {
        finish();
    }
}
