package com.creative.share.apps.wash_squad_driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_cancel_order.CancelOrderActivity;
import com.creative.share.apps.wash_squad_driver.databinding.AddtionServiceRowBinding;
import com.creative.share.apps.wash_squad_driver.databinding.ResonRowBinding;
import com.creative.share.apps.wash_squad_driver.models.Resson_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AdditionalServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Resson_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
private int i=0;
    public AdditionalServiceAdapter(List<Resson_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        AddtionServiceRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.addtion_service_row,parent,false);
            return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Resson_Model.Data order_data = orderlist.get(position);

            EventHolder eventHolder = (EventHolder) holder;




    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public  AddtionServiceRowBinding binding;
        public EventHolder(@NonNull AddtionServiceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }




}
