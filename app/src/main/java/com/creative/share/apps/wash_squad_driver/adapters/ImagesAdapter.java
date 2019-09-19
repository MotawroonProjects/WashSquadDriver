package com.creative.share.apps.wash_squad_driver.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_cancel_order.CancelOrderActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details.Finish_OrderDetailsActivity;
import com.creative.share.apps.wash_squad_driver.databinding.ImageRowBinding;
import com.creative.share.apps.wash_squad_driver.databinding.ResonRowBinding;
import com.creative.share.apps.wash_squad_driver.models.Order_Images_Model;
import com.creative.share.apps.wash_squad_driver.models.Resson_Model;
import com.creative.share.apps.wash_squad_driver.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order_Images_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Finish_OrderDetailsActivity activity;
private int i=0;
    public ImagesAdapter(List<Order_Images_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (Finish_OrderDetailsActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ImageRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.image_row,parent,false);
            return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order_Images_Model.Data order_data = orderlist.get(position);

            EventHolder eventHolder = (EventHolder) holder;
//eventHolder.binding.setLang(lang);
//eventHolder.binding.setReasonmodel(order_data);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+order_data.getImage())).fit().placeholder(activity.getResources().getDrawable(R.drawable.ic_car)).into(eventHolder.binding.image);
        if(order_data.getType().equals("1")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.tire));
        }
        else  if(order_data.getType().equals("2")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.outside));
        }
        else  if(order_data.getType().equals("3")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.doors));
        }
        else  if(order_data.getType().equals("4")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.tablon));
        }
        else  if(order_data.getType().equals("5")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.steas));
        }
        else  if(order_data.getType().equals("6")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.roof));
        }
        else  if(order_data.getType().equals("7")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.floor));
        }
        else  if(order_data.getType().equals("8")){
            eventHolder.binding.tvTitle.setText(activity.getResources().getString(R.string.glass));
        }


    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public  ImageRowBinding binding;
        public EventHolder(@NonNull ImageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }




}
