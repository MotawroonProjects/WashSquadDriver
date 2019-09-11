package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_cancel_order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityCancelOrderBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;

import io.paperdb.Paper;

public class CancelOrderActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityCancelOrderBinding binding;
    private int refuse_reason=0;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,"en"));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_cancel_order);
        initView();
    }

    private void initView() {
        binding.setBackListener(this);
        binding.rbCustom.setOnClickListener(view -> refuse_reason = 1);
        binding.rbTransformation.setOnClickListener(view -> refuse_reason = 2);
        binding.rbIssue.setOnClickListener(view -> refuse_reason = 3);
        binding.btnDone.setOnClickListener(view -> {
            if (refuse_reason!=0)
            {
                Intent intent = getIntent();
                if (intent!=null)
                {
                    intent.putExtra("reason",refuse_reason);
                    setResult(RESULT_OK,intent);
                }
                finish();
            }else 
                {
                    Toast.makeText(this, R.string.ch_reson_cancel, Toast.LENGTH_LONG).show();
                }
        });

    }

    @Override
    public void back() {
        finish();
    }
}
