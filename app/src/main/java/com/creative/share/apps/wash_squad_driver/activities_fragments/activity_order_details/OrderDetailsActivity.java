package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_Step1;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity_step4;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityOrderDetailsBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;

import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private static final int REQUEST_PHONE_CALL = 1;

    private ActivityOrderDetailsBinding binding;
    private Order_Model.Data data;
    private String lang;
    Intent intent;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, "en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        initView();
    }

    private void initView() {
        binding.setBackListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        data = (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
        binding.setLang(lang);
        binding.setOrderModel(data);
        if (data.getUser_phone() != null && data.getUser_phone_code() != null) {
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getUser_phone_code().replaceFirst("00", "+") + data.getUser_phone(), null));
        }

        binding.btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getStatus() == 1) {
                    start();
                } else if (data.getStatus() == 2) {
                    Intent intent = new Intent(OrderDetailsActivity.this, Work2Activity.class);
                    intent.putExtra("detials", data);
                    startActivityForResult(intent, 1002);
                    finish();
                }
            }
        });
        binding.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent != null) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(OrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(OrderDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1002 || requestCode == 1003) && resultCode == RESULT_OK && data != null) {
            Intent intent = getIntent();
            if (intent != null) {
                intent.putExtra("reason", 1);
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }

    private void start() {
        final Dialog dialog = Common.createProgressDialog(OrderDetailsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        try {
            Api.getService(lang, Tags.base_url)
                    .start(data.getId() + "", (Calendar.getInstance().getTimeInMillis() / 1000) + "").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(OrderDetailsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        Intent intent = new Intent(OrderDetailsActivity.this, Work2Activity.class);
                        intent.putExtra("detials", data);

                        startActivityForResult(intent, 1003);
                        finish();
                    } else {
                        try {

                            Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.code() + "" + response.message() + "" + response.errorBody().string() + response.raw() + response.body() + response.headers());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(OrderDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }
}
