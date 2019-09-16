package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityFinishOrderDetailsBinding;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityOrderDetailsBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;

import java.util.Locale;

import io.paperdb.Paper;

public class Finish_OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityFinishOrderDetailsBinding binding;
private Order_Model.Data data;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,"en"));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_finish_order_details);
        initView();
    }

    private void initView() {
        binding.setBackListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        data= (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
binding.setLang(lang);
binding.setOrderModel(data);


    }

    @Override
    public void back() {
        finish();
    }


}
