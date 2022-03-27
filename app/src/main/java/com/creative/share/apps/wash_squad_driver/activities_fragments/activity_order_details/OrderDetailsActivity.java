package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_payment.PaymentActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_Step1;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity_step4;
import com.creative.share.apps.wash_squad_driver.adapters.AdditionalServiceAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityOrderDetailsBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Data_Model;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timerx.Stopwatch;
import timerx.StopwatchBuilder;
import timerx.Timer;
import timerx.TimerBuilder;

public class OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private static final int REQUEST_PHONE_CALL = 1;

    private ActivityOrderDetailsBinding binding;
    private Order_Model.Data data;
    private String lang;
    Intent intent;
    private Chronometer chronometer;
    private Order_Data_Model.OrderModel orderModel;
    private AdditionalServiceAdapter additionalServiceAdapter;
    private List<Order_Model.Data.Services> servicesList;

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
        servicesList = new ArrayList<>();
        additionalServiceAdapter = new AdditionalServiceAdapter(servicesList, this);
        // binding.time.setFormat("Formated Time - %s");
        binding.setBackListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        data = (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
        binding.setLang(lang);
        binding.setOrderModel(data);


//        final Handler handler
//                = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
//        handler.post(new Runnable() {
//            @Override
//
//            public void run()
//            {
//                int hours = seconds / 3600;
//                int minutes = (seconds % 3600) / 60;
//                int secs = seconds % 60;
//
//                // Format the seconds into hours, minutes,
//                // and seconds.
//                String time
//                        = String
//                        .format(Locale.getDefault(),
//                                "%d:%02d:%02d", hours,
//                                minutes, secs);
//
//                // Set the text view text.
//                timeView.setText(time);
//
//                // If running is true, increment the
//                // seconds variable.
//                if (running) {
//                    seconds++;
//                }
//
//                // Post the code again
//                // with a delay of 1 second.
//                handler.postDelayed(this, 1000);
//            }
//        });

        // binding.circleTimerView.startTimer();
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));
        binding.recView.setAdapter(additionalServiceAdapter);
        binding.btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getStatus() == 12) {
                    start();
                } else if (data.getStatus() == 2) {
                    binding.circleTimerView.stopTimer();
                    //step2("");
                    Intent intent = new Intent(OrderDetailsActivity.this, PaymentActivity.class);
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
        getOrder();
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
                    .start(data.getId() + "", (System.currentTimeMillis() / 1000) + "").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(OrderDetailsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                        //binding.time.setBase(System.currentTimeMillis());
                        data.setStatus(2);
                        binding.circleTimerView.stopTimer();
                        binding.btShow.setText(getResources().getString(R.string.done));

                        //  adsActivity.finish(response.body().getId_advertisement());
//                        Intent intent = new Intent(OrderDetailsActivity.this, Work2Activity.class);
//                        intent.putExtra("detials", data);
//
//                        startActivityForResult(intent, 1003);
//                        finish();
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

//    private void step2(String feedback) {
//        final Dialog dialog = Common.createProgressDialog(OrderDetailsActivity.this, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        try {
//
//
//            Api.getService(lang, Tags.base_url)
//                    .Step2(data.getId() + "", (Calendar.getInstance().getTimeInMillis() / 1000) + "", feedback).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    dialog.dismiss();
//                    if (response.isSuccessful()) {
//                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
//                        Toast.makeText(OrderDetailsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
//
//                        //  adsActivity.finish(response.body().getId_advertisement());
//
//                        finish();
//                    } else {
//                        try {
//
//                            Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            Log.e("Error", response.code() + "" + response.errorBody().string() + response.raw() + response.body() + response.headers());
//                        } catch (Exception e) {
//
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dialog.dismiss();
//                    try {
//                        Toast.makeText(OrderDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                        Log.e("Error", t.getMessage());
//                    } catch (Exception e) {
//
//                    }
//                }
//            });
//        } catch (Exception e) {
//            dialog.dismiss();
//        }
//    }

    private void getOrder() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(lang, Tags.base_url)
                    .getOrdersById(data.getId() + "")
                    .enqueue(new Callback<Order_Model.Data>() {
                        @Override
                        public void onResponse(Call<Order_Model.Data> call, Response<Order_Model.Data> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                data = response.body();
                                updateUi(data);

                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(OrderDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    try {
                                        Log.e("errorsssss", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Model.Data> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(OrderDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void updateUi(Order_Model.Data orderModel) {
        data = orderModel;
//        String times = "0";
//        if (data.getStart_time_work() != null) {
//            times = data.getStart_time_work();
//            Log.e("lllll", System.currentTimeMillis() + " " + (Long.parseLong(times) * 1000) + " " + (System.currentTimeMillis() - (Long.parseLong(times) * 1000)));
//
//            long milliseconds = System.currentTimeMillis() - (Long.parseLong(times) * 1000);
//            long minutes = (milliseconds / 1000) / 60;
//
//            // formula for conversion for
//            // milliseconds to seconds
//            long seconds = (milliseconds / 1000) % 60;
//            binding.time.setBase(SystemClock.elapsedRealtime() - (minutes * 60000 + seconds * 1000));
//            binding.time.start();
//            binding.btShow.setText(getResources().getString(R.string.done));
//        }
        if (orderModel.getStart_time_work() != null) {
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");

            binding.circleTimerView.setCurrentTime(orderModel.getService().getTimer() * 60 * 1000);
            binding.circleTimerView.startTimer();


            //  binding.circleTimerView.setTimeFormat(1);

        }
        if (data.getStatus() == 2) {
            binding.btShow.setText(getResources().getString(R.string.done));
        }
        servicesList.addAll(orderModel.getSub_service());
        additionalServiceAdapter.notifyDataSetChanged();
        binding.setOrderModel(data);
        if (data.getUser_phone() != null && data.getUser_phone_code() != null) {
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getUser_phone_code().replaceFirst("00", "+") + data.getUser_phone(), null));
        }

    }

}
