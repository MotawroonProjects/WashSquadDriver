package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityOrderDetailsBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;

import org.androidannotations.annotations.OnActivityResult;

import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityOrderDetailsBinding binding;
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_details);
        initView();
    }

    private void initView() {
        binding.setBackListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        data= (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
binding.setLang(lang);
binding.setOrderModel(data);
if(data.getStatus()!=1){
    binding.btShow.setVisibility(View.GONE);
}
else {
    binding.lll.setVisibility(View.GONE);
}
binding.btShow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(data.getStatus()==1){
        Intent intent = new Intent(OrderDetailsActivity.this, Work1Activity.class);
        intent.putExtra("detials",data);

        startActivityForResult(intent,1002);

        }
        else if(data.getStatus()==2){
            Intent intent = new Intent(OrderDetailsActivity.this, Work2Activity.class);
            intent.putExtra("detials",data);

            startActivityForResult(intent,1003);
        }
    }
});
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 1002 && resultCode == RESULT_OK && data != null) {
             Intent intent = getIntent();
             if (intent!=null)
             {
                 intent.putExtra("reason",1);
                 setResult(RESULT_OK,intent);
             }
           finish();
        }
    }
}
