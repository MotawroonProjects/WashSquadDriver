package com.creative.share.apps.wash_squad_driver.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.fragments.FragmentCustomerStatictis;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_statistic.fragments.FragmentOrderStatictis;
import com.creative.share.apps.wash_squad_driver.databinding.MonthRowBinding;
import com.creative.share.apps.wash_squad_driver.models.MonthModel;
import com.creative.share.apps.wash_squad_driver.models.Resson_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MonthModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int currentPos = -1;
    private int oldPos;
    private Fragment fragment;

    public MonthAdapter(List<MonthModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        MonthRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.month_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Resson_Model.Data order_data = orderlist.get(position);

        EventHolder eventHolder = (EventHolder) holder;

        eventHolder.binding.setModel(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPos = holder.getLayoutPosition();

                MonthModel monthModel = list.get(oldPos);
                monthModel.setIsselected(false);
                notifyItemChanged(oldPos, monthModel);
                MonthModel model = list.get(currentPos);
                model.setIsselected(true);
                oldPos = currentPos;

                notifyItemChanged(currentPos, model);
                // notifyDataSetChanged();
                if (fragment instanceof FragmentCustomerStatictis) {
                    FragmentCustomerStatictis fragmentCustomerStatictis = (FragmentCustomerStatictis) fragment;
                    fragmentCustomerStatictis.getReviews((holder.getLayoutPosition() + 1) + "");
                } else if (fragment instanceof FragmentOrderStatictis) {
                    FragmentOrderStatictis fragmentOrderStatictis = (FragmentOrderStatictis) fragment;
                    fragmentOrderStatictis.getReviews((holder.getLayoutPosition() + 1) + "");
                }        // Log.e("d'd;d;;d", oldPos + "");


                //notifyItemChanged(currentPos);


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public MonthRowBinding binding;

        public EventHolder(@NonNull MonthRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
