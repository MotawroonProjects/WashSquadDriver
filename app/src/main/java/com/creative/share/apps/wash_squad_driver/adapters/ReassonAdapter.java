package com.creative.share.apps.wash_squad_driver.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_cancel_order.CancelOrderActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.databinding.OrderRowBinding;
import com.creative.share.apps.wash_squad_driver.databinding.ResonRowBinding;
import com.creative.share.apps.wash_squad_driver.databinding.ServiceRowBinding;
import com.creative.share.apps.wash_squad_driver.models.Resson_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ReassonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Resson_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private CancelOrderActivity activity;
private int i=0;
    public ReassonAdapter(List<Resson_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (CancelOrderActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ResonRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.reson_row,parent,false);
            return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Resson_Model.Data order_data = orderlist.get(position);

            EventHolder eventHolder = (EventHolder) holder;
eventHolder.binding.setLang(lang);
eventHolder.binding.setReasonmodel(order_data);
eventHolder.binding.rbCustom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        i=position;
        activity.setreasson(orderlist.get(holder.getLayoutPosition()).getId());
        notifyDataSetChanged();
    }
});
if(position==i){
    eventHolder.binding.rbCustom.setChecked(true);
}
else {
    eventHolder.binding.rbCustom.setChecked(false);

}



    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public  ResonRowBinding binding;
        public EventHolder(@NonNull ResonRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }




}
