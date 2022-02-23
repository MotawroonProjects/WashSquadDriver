package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details.Finish_OrderDetailsActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.activity.StatictisActivity;
import com.creative.share.apps.wash_squad_driver.adapters.MonthAdapter;
import com.creative.share.apps.wash_squad_driver.adapters.MyOrdrrAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.FragmentCustomerStatisticsBinding;
import com.creative.share.apps.wash_squad_driver.databinding.FragmentOrderBinding;
import com.creative.share.apps.wash_squad_driver.models.MonthModel;
import com.creative.share.apps.wash_squad_driver.models.RateModel;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.models.RateModel;
import com.creative.share.apps.wash_squad_driver.models.ReviewModel;
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

public class FragmentCustomerStatictis extends Fragment {
    private StatictisActivity activity;
    private FragmentCustomerStatisticsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<MonthModel> list;
    private MonthAdapter monthAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_statistics, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        activity = (StatictisActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        list = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        monthAdapter = new MonthAdapter(list, activity, this);
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
                    .getreviews(month, userModel.getId())
                    .enqueue(new Callback<RateModel>() {
                        @Override
                        public void onResponse(Call<RateModel> call, Response<RateModel> response) {
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
                        public void onFailure(Call<RateModel> call, Throwable t) {
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

    private void updateData(RateModel body) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setRate1(new ReviewModel.Rate());
        reviewModel.setRate2(new ReviewModel.Rate());
        reviewModel.setRate3(new ReviewModel.Rate());
        reviewModel.setRate4(new ReviewModel.Rate());
        reviewModel.setRate5(new ReviewModel.Rate());

        if (body.getData().size() > 0) {
            for (int i = 0; i < body.getData().size(); i++) {
                if (body.getData().get(i).getRating() == 1) {
                    ReviewModel.Rate rate1 = reviewModel.getRate1();
                    rate1.setRating(body.getData().get(i).getRating());
                    rate1.setCommission_value(body.getData().get(i).getCommission_value());
                    rate1.setSum_of_commission(body.getData().get(i).getSum_of_commission());
                    rate1.setCount_of_orders(body.getData().get(i).getcount_of_orders());
                    reviewModel.setRate1(rate1);
                } else if (body.getData().get(i).getRating() == 2) {
                    ReviewModel.Rate rate2 = reviewModel.getRate2();
                    rate2.setRating(body.getData().get(i).getRating());
                    rate2.setCommission_value(body.getData().get(i).getCommission_value());
                    rate2.setSum_of_commission(body.getData().get(i).getSum_of_commission());
                    rate2.setCount_of_orders(body.getData().get(i).getcount_of_orders());
                    reviewModel.setRate2(rate2);
                } else if (body.getData().get(i).getRating() == 3) {
                    ReviewModel.Rate rate3 = reviewModel.getRate3();
                    rate3.setRating(body.getData().get(i).getRating());
                    rate3.setCommission_value(body.getData().get(i).getCommission_value());
                    rate3.setSum_of_commission(body.getData().get(i).getSum_of_commission());
                    rate3.setCount_of_orders(body.getData().get(i).getcount_of_orders());
                    reviewModel.setRate3(rate3);
                } else if (body.getData().get(i).getRating() == 4) {
                    ReviewModel.Rate rate4 = reviewModel.getRate4();
                    rate4.setRating(body.getData().get(i).getRating());
                    rate4.setCommission_value(body.getData().get(i).getCommission_value());
                    rate4.setSum_of_commission(body.getData().get(i).getSum_of_commission());
                    rate4.setCount_of_orders(body.getData().get(i).getcount_of_orders());
                    reviewModel.setRate4(rate4);
                } else if (body.getData().get(i).getRating() == 5) {
                    ReviewModel.Rate rate5 = reviewModel.getRate5();
                    rate5.setRating(body.getData().get(i).getRating());
                    rate5.setCommission_value(body.getData().get(i).getCommission_value());
                    rate5.setSum_of_commission(body.getData().get(i).getSum_of_commission());
                    rate5.setCount_of_orders(body.getData().get(i).getcount_of_orders());
                    reviewModel.setRate5(rate5);
                }
            }
        }
        binding.setModel(reviewModel);

    }

    public static FragmentCustomerStatictis newInstance() {
        return new FragmentCustomerStatictis();
    }

}
