package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_cancel_order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.adapters.MyOrdrrAdapter;
import com.creative.share.apps.wash_squad_driver.adapters.ReassonAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityCancelOrderBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.models.Resson_Model;
import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelOrderActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityCancelOrderBinding binding;
    private int refuse_reason=0;
    private ReassonAdapter reassonAdapter;
    private List<Resson_Model.Data> reData;
    private String lang;
    private Order_Model.Data data;

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
        getResson();
        
    }
    public void getResson() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        binding.progBar.setVisibility(View.VISIBLE);
        // rec_sent.setVisibility(View.GONE);
        Api.getService(lang, Tags.base_url)
                .getreasson()
                .enqueue(new Callback<Resson_Model>() {
                    @Override
                    public void onResponse(Call<Resson_Model> call, Response<Resson_Model> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            reData.clear();
                            reData.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_`````````````````    `   ``````````  `   ```                                                                                                         sent.setVisibility(View.VISIBLE);
                                //  Log.e("data",response.body().getData().get(0).getAr_title());

                                binding.llNoOrders.setVisibility(View.GONE);
                                reassonAdapter.notifyDataSetChanged();
                                setreasson(response.body().getData().get(0).getId());
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                binding.llNoOrders.setVisibility(View.VISIBLE);

                            }
                        } else {
                            binding.llNoOrders.setVisibility(View.VISIBLE);

                            Toast.makeText(CancelOrderActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Resson_Model> call, Throwable t) {
                        try {


                            Toast.makeText(CancelOrderActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        data= (Order_Model.Data) getIntent().getExtras().getSerializable("detials");

        binding.setBackListener(this);
        reData=new ArrayList<>();
        reassonAdapter=new ReassonAdapter(reData,this);
        binding.recResson.setLayoutManager(new GridLayoutManager(this,1));
        binding.recResson.setAdapter(reassonAdapter);
        binding.btnDone.setOnClickListener(view -> {
            if (refuse_reason!=0)
            {

                sendereason();
            }else 
                {
                    Toast.makeText(this, R.string.ch_reson_cancel, Toast.LENGTH_LONG).show();
                }
        });

    }

    private void sendereason() {
        final ProgressDialog dialog = Common.createProgressDialog(CancelOrderActivity.this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(lang,Tags.base_url)
                    .Csncel_order(data.getId(),refuse_reason)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                Intent intent = getIntent();
                                if (intent!=null)
                                {
                                    intent.putExtra("reason",refuse_reason);
                                    setResult(RESULT_OK,intent);
                                }
                                finish();

                            }

                            else
                            {
                                Toast.makeText(CancelOrderActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
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
                                        Toast.makeText(CancelOrderActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CancelOrderActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void back() {
        finish();
    }

    public void setreasson(int id) {
        refuse_reason=id;
    }
}
