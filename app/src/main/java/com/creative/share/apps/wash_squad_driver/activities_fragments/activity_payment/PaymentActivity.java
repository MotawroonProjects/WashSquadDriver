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
            payment_method = 2;
        });

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
