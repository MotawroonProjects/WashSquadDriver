package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.adapters.ImagesAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityFinishOrderDetailsBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Images_Model;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Finish_OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityFinishOrderDetailsBinding binding;
    private Order_Model.Data data;
    private String lang;
    private List<Order_Images_Model.Data> dataList1, dataList2;
    private ImagesAdapter imagesAdapter, imagesAdapter2;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, "en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finish_order_details);
        getOrders(1);
        getOrders(2);
        initView();
    }

    private void initView() {
        dataList1 = new ArrayList<>();
        dataList2 = new ArrayList<>();
        binding.setBackListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        data = (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
        binding.setLang(lang);
        binding.setOrderModel(data);
        imagesAdapter=new ImagesAdapter(dataList1,this);
        imagesAdapter2=new ImagesAdapter(dataList2,this);
        binding.recBefore.setItemViewCacheSize(25);
        binding.recBefore.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recBefore.setDrawingCacheEnabled(true);
        binding.recAfter.setItemViewCacheSize(25);
        binding.recAfter.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recAfter.setDrawingCacheEnabled(true);
        binding.recBefore.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.recAfter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));

        binding.recBefore.setAdapter(imagesAdapter);
binding.recAfter.setAdapter(imagesAdapter2);

    }
    public void getOrders(int status) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(lang,Tags.base_url)
                    .MyOrderimages(data.getId(),status)
                    .enqueue(new Callback<Order_Images_Model>() {
                        @Override
                        public void onResponse(Call<Order_Images_Model> call, Response<Order_Images_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

//                                    binding.llNoorder.setVisibility(View.GONE);
                                    if(status==1){
                                        dataList1.clear();
                                        dataList1.addAll(response.body().getData());
                                    imagesAdapter.notifyDataSetChanged();}
                                    else if(status==2){
                                        dataList2.clear();
                                        dataList2.addAll(response.body().getData());
                                        imagesAdapter2.notifyDataSetChanged();
                                    }
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                              //      binding.llNoorder.setVisibility(View.VISIBLE);

                                }
                            } else {
                            //    binding.llNoorder.setVisibility(View.VISIBLE);

                                //   Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Images_Model> call, Throwable t) {
                            try {
                                // binding.progBar.setVisibility(View.GONE);
                              //  binding.llNoorder.setVisibility(View.VISIBLE);


                                Toast.makeText(Finish_OrderDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
           // binding.llNoorder.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void back() {
        finish();
    }


}
