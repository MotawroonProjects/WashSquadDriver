package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.adapters.MyOrdrrAdapter;
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

public class Fragment_Canceled_Order extends Fragment {
    private HomeActivity activity;
    private FragmentOrderBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private MyOrdrrAdapter myOrdrrAdapter;
    private List<Order_Model.Data> oDataList;
    private GridLayoutManager manager;
    private String lang;
    private boolean isLoading = false;
    private int current_page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        initView();
        if (userModel != null) {
            getOrders();
        } else {
            binding.llNoOrders.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    private void initView() {
        oDataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        myOrdrrAdapter = new MyOrdrrAdapter(oDataList, activity, this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        manager = new GridLayoutManager(activity, 1);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(myOrdrrAdapter);
        binding.progBar.setVisibility(View.GONE);
        binding.llNoOrders.setVisibility(View.GONE);
        binding.swipeContainer.setOnClickListener(v -> {
            if (userModel != null) {
                getOrders();
            } else {
                binding.llNoOrders.setVisibility(View.VISIBLE);
            }        });
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int totalItems = myOrdrrAdapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();

                    if (totalItems > 10 && (totalItems - lastVisiblePos) == 3 && !isLoading) {
                        isLoading = true;
                        oDataList.add(null);
                        myOrdrrAdapter.notifyItemInserted(oDataList.size() - 1);
                        if (userModel != null) {
                            int page = Fragment_Canceled_Order.this.current_page + 1;
                            loadMore(page);
                        }
                    }


                }
            }
        });


    }


    public static Fragment_Canceled_Order newInstance() {
        return new Fragment_Canceled_Order();
    }

    public void getOrders() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        try {


            binding.progBar.setVisibility(View.VISIBLE);
            // rec_sent.setVisibility(View.GONE);
            Api.getService(lang, Tags.base_url)
                    .MyOrder(1, userModel.getId(), 5)
                    .enqueue(new Callback<Order_Model>() {
                        @Override
                        public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                oDataList.clear();
                                oDataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoOrders.setVisibility(View.GONE);
                                    myOrdrrAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    binding.llNoOrders.setVisibility(View.VISIBLE);

                                }
                            } else {
                                binding.llNoOrders.setVisibility(View.VISIBLE);

                                // Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Model> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoOrders.setVisibility(View.VISIBLE);


                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.llNoOrders.setVisibility(View.VISIBLE);

        }

    }

    private void loadMore(int page) {
        try {


            Api.getService(lang, Tags.base_url)
                    .MyOrder(page, userModel.getId(), 5)
                    .enqueue(new Callback<Order_Model>() {
                        @Override
                        public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                            oDataList.remove(oDataList.size() - 1);
                            myOrdrrAdapter.notifyItemRemoved(oDataList.size() - 1);
                            isLoading = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                oDataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                Fragment_Canceled_Order.this.current_page = response.body().getCurrent_page();
                                myOrdrrAdapter.notifyDataSetChanged();

                            } else {
                                //   Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Model> call, Throwable t) {
                            try {
                                oDataList.remove(oDataList.size() - 1);
                                myOrdrrAdapter.notifyItemRemoved(oDataList.size() - 1);
                                isLoading = false;
                                //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            oDataList.remove(oDataList.size() - 1);
            myOrdrrAdapter.notifyItemRemoved(oDataList.size() - 1);
            isLoading = false;
        }
    }
}
