package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Canceled_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Current_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Previous_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.wash_squad_driver.adapters.MyPagerAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityHomeBinding;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.preferences.Preferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private MyPagerAdapter adapter;

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
        userModel = preferences.getUserData(this);
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

    }

    private void navigateToSinInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
