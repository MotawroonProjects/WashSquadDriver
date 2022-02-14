package com.creative.share.apps.wash_squad_driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.databinding.MonthRowBinding;
import com.creative.share.apps.wash_squad_driver.models.Resson_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int currentPos = 0;
    private int oldPos = currentPos;

    public MonthAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
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

        eventHolder.binding.setTitle(list.get(position));

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                currentPos = holder.getLayoutPosition();
//                BranchModel model = list.get(currentPos);
//
//                if (!model.isSelected()) {
//
//                    BranchModel oldModel = list.get(oldPos);
//                    oldModel.setSelected(false);
//                    list.set(oldPos, oldModel);
//                    notifyItemChanged(oldPos);
//
//                    model.setSelected(true);
//                    list.set(currentPos, model);
//                    oldPos = currentPos;
//                    notifyItemChanged(currentPos);
//                    // notifyDataSetChanged();
//
//                }
//                // Log.e("d'd;d;;d", oldPos + "");
//
//
//                //notifyItemChanged(currentPos);
//
//
//            }
//        });

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
