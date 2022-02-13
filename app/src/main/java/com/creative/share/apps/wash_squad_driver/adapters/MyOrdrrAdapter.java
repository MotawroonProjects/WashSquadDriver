package com.creative.share.apps.wash_squad_driver.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Canceled_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Current_Order;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.fragments.Fragment_Previous_Order;
import com.creative.share.apps.wash_squad_driver.databinding.LoadMoreBinding;
import com.creative.share.apps.wash_squad_driver.databinding.OrderRowBinding;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MyOrdrrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<Order_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private HomeActivity activity;
    private Fragment fragment;

    public MyOrdrrAdapter(List<Order_Model.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (HomeActivity) context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
            return new EventHolder(binding);

        } else {
            LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more, parent, false);
            return new LoadHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order_Model.Data order_data = orderlist.get(position);
        if (holder instanceof EventHolder) {
            EventHolder eventHolder = (EventHolder) holder;
            eventHolder.binding.setLang(lang);
            eventHolder.binding.setOrderModel(order_data);
            if (order_data.getStatus() == 5) {
                eventHolder.binding.llReason.setVisibility(View.GONE);
            } else {
                eventHolder.binding.llReason.setVisibility(View.VISIBLE);

            }
            Log.e("data", order_data.getId() + " " + order_data.getStatus());
            eventHolder.binding.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.makecall(orderlist.get(holder.getLayoutPosition()));
                }
            });
            eventHolder.binding.btnAccept.setOnClickListener(view -> {
                if (fragment instanceof Fragment_Current_Order) {


                    activity.Show_Detials(orderlist.get(holder.getLayoutPosition()));
                } else if (fragment instanceof Fragment_Previous_Order) {
                    activity.Show_Detialsdata(orderlist.get(holder.getLayoutPosition()));
                }
            });


        } else {
            LoadHolder loadHolder = (LoadHolder) holder;
            loadHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public EventHolder(@NonNull OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            if (fragment instanceof Fragment_Canceled_Order) {
                this.binding.btnAccept.setVisibility(View.GONE);
            }
            if (fragment instanceof Fragment_Previous_Order) {
                this.binding.btnAccept.setText(activity.getResources().getString(R.string.details));
            }
        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;

        public LoadHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Order_Model.Data order_Model = orderlist.get(position);
        if (order_Model != null) {
            return ITEM_DATA;
        } else {
            return LOAD;
        }

    }


}
