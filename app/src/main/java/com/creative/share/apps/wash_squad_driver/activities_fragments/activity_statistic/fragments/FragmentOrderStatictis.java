package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.activity.StatictisActivity;
import com.creative.share.apps.wash_squad_driver.adapters.MonthAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.FragmentOrderStatisticsBinding;
import com.creative.share.apps.wash_squad_driver.models.MonthModel;
import com.creative.share.apps.wash_squad_driver.models.StaticModel;
import com.creative.share.apps.wash_squad_driver.models.StaticModel;
import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.preferences.Preferences;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOrderStatictis extends Fragment {
    private StatictisActivity activity;
    private FragmentOrderStatisticsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<MonthModel> list;
    private MonthAdapter monthAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_statistics, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        list=new ArrayList<>();
        activity = (StatictisActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        monthAdapter=new MonthAdapter(list,activity,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(monthAdapter);
        addmonth();



    }

    private void addmonth() {
        MonthModel monthModel = new MonthModel("JAN");
        monthModel.setIsselected(true);
        list.add(monthModel);
        list.add(new MonthModel("FEB"));
        list.add(new MonthModel("MAR"));
        list.add(new MonthModel("APR"));
        list.add(new MonthModel("MAY"));
        list.add(new MonthModel("JUN"));
        list.add(new MonthModel("JUL"));
        list.add(new MonthModel("AUG"));
        list.add(new MonthModel("SEP"));
        list.add(new MonthModel("OCT"));
        list.add(new MonthModel("NOV"));
        list.add(new MonthModel("DEC"));
        monthAdapter.notifyDataSetChanged();
        getReviews("1");

    }
    public void getReviews(String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        month = (calendar.get(Calendar.YEAR) + "-" + month);
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(lang, Tags.base_url)
                    .getStatistic(month, userModel.getId())
                    .enqueue(new Callback<StaticModel>() {
                        @Override
                        public void onResponse(Call<StaticModel> call, Response<StaticModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                // rec_sent.setVisibility(View.VISIBLE);
                                //  Log.e("data",response.body().getData().get(0).getAr_title());

//                                    binding.llNoorder.setVisibility(View.GONE);

                                //   total_page = response.body().getMeta().getLast_page();

                                updateData(response.body());

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
                        public void onFailure(Call<StaticModel> call, Throwable t) {
                            try {
                                // binding.progBar.setVisibility(View.GONE);
                                //  binding.llNoorder.setVisibility(View.VISIBLE);


                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
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

    private void updateData(StaticModel body) {
        binding.setModel(body);
    }


    public static FragmentOrderStatictis newInstance() {
        return new FragmentOrderStatictis();
    }


}
