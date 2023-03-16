package com.alphabetlore3d.simsoundboard.billing.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabetlore3d.simsoundboard.R;
import com.alphabetlore3d.simsoundboard.billing.interfaces.RecycleViewInterface;
import com.android.billingclient.api.ProductDetails;

import java.util.List;

public class SubsProductsAdapter extends RecyclerView.Adapter<SubsProductsAdapter.ExampleViewHolder> {

    private List<ProductDetails> exampleList;
    RecycleViewInterface callBack;

    public SubsProductsAdapter(List<ProductDetails> exampleList, RecycleViewInterface callBack) {
        this.exampleList = exampleList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subscription, parent, false);
        return new ExampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductDetails currentItem = exampleList.get(position);

        holder.textView1.setText(currentItem.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.text_view_1);
        }
    }
}