package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.fragments;

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
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.activity.StatictisActivity;
import com.creative.share.apps.wash_squad_driver.adapters.MonthAdapter;
import com.creative.share.apps.wash_squad_driver.adapters.MyOrdrrAdapter;
import com.creative.share.apps.wash_squad_driver.databinding.FragmentCustomerStatisticsBinding;
import com.creative.share.apps.wash_squad_driver.databinding.FragmentOrderBinding;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.preferences.Preferences;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
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
    private List<String> list;
    private MonthAdapter monthAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_statistics, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        list=new ArrayList<>();
        addmonth();
        activity = (StatictisActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        monthAdapter=new MonthAdapter(list,activity);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(monthAdapter);


    }

    private void addmonth() {
        list.add("JAN");
        list.add("FEB");

    }


    public static FragmentCustomerStatictis newInstance() {
        return new FragmentCustomerStatictis();
    }

}
