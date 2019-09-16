package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Canceled_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Current_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Previous_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details.Finish_OrderDetailsActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.wash_squad_driver.adapters.MyPagerAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityHomeBinding;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.preferences.Preferences;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private MyPagerAdapter adapter;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,"en"));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initView();




    }


    private void initView() {
        Paper.init(this);
        preferences = Preferences.newInstance();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        userModel = preferences.getUserData(this);
        binding.setUserModel(userModel);
        String lastVisit = preferences.getLastVisit(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String now = dateFormat.format(new Date(Calendar.getInstance().getTimeInMillis()));

        if (!lastVisit.equals(now))
        {
            updateVisit(now,(Calendar.getInstance().getTimeInMillis()/1000));

        }

        binding.setTitle(getString(R.string.order2));
        binding.tab.setupWithViewPager(binding.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(getFragments());
        adapter.addTitles(getTitles());
        binding.pager.setAdapter(adapter);
binding.tvLogout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        logout();
    }
});
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0)
                {
                    binding.setTitle(getString(R.string.order2));

                }else if (position==1)
                {
                    binding.setTitle(getString(R.string.finished));

                }else if (position==2)
                {
                    binding.setTitle(getString(R.string.canceled));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private void updateVisit(String now, long time) {
        /*Api.getService(Tags.base_url)
                .updateVisit(time,2)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            preferences.setLastVisit(HomeActivity.this,now);
                        }else
                        {
                            try {
                                Log.e("errorVisitCode",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage()+"_");
                        }catch (Exception e){}
                    }
                });*/
    }


    private List<Fragment> getFragments()
    {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(Fragment_Current_Order.newInstance());
        fragmentList.add(Fragment_Previous_Order.newInstance());
        fragmentList.add(Fragment_Canceled_Order.newInstance());
        return fragmentList;

    }

    private List<String> getTitles()
    {
        List<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.order2));
        titleList.add(getString(R.string.finished));
        titleList.add(getString(R.string.canceled));
        return titleList;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 ) {
            Intent intent = getIntent();
           startActivity(intent);
            finish();
        }
    }

    private void navigateToSinInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    private void logout()
    {

      /*  Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();*/
//Log.e("llll","kkkkkk");
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(lang, Tags.base_url)
                    .logout(userModel.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                               // Log.e("token",response.body().getName());
                                preferences.create_update_userData(HomeActivity.this, null);
                                preferences.createSession(HomeActivity.this, Tags.session_logout);
                                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish();

                            }else
                            {
                                Log.e("llll","kkkkkk");

                               /* if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
                                } else*/ if (response.code() == 500) {
                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(HomeActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();
            Log.e("lll",e.getMessage().toString());
        }
    }

    public void Show_Detials(Order_Model.Data data) {
        Intent intent=new Intent(HomeActivity.this, MapActivity.class);
        intent.putExtra("detials",data);
        startActivityForResult(intent,1002);


    }

    public void Show_Detialsdata(Order_Model.Data data) {
        Intent intent=new Intent(HomeActivity.this, Finish_OrderDetailsActivity.class);
        intent.putExtra("detials",data);
        startActivity(intent);
    }

}
