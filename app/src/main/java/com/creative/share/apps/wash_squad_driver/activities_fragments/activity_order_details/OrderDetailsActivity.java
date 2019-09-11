package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityOrderDetailsBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;

import io.paperdb.Paper;

public class OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityOrderDetailsBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,"en"));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_details);
        initView();
    }

    private void initView() {
        binding.setBackListener(this);


    }

    @Override
    public void back() {
        finish();
    }
}
