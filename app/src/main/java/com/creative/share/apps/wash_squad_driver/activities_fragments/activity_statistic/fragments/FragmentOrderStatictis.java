package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class FragmentOrderStatictis extends Fragment {
    private StatictisActivity activity;
    private FragmentOrderStatisticsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<String> list;
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
        addmonth();
        activity = (StatictisActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        monthAdapter=new MonthAdapter(list,activity);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, true));
        binding.recView.setAdapter(monthAdapter);


    }

    private void addmonth() {
        list.add("JAN");
    }


    public static FragmentOrderStatictis newInstance() {
        return new FragmentOrderStatictis();
    }


}
