package com.alphabetlore3d.simsoundboard.b;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabetlore3d.simsoundboard.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter {
    public static List<More> webLists;
    private final int VIEW_ITEM = 0;
    public Context context;


    public MoreAdapter(List<More> webLists, Context context) {
        MoreAdapter.webLists = webLists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lm_, parent, false);
            return new ViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lp_, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            final More webList = webLists.get(position);
            ((ViewHolder) holder).html_url.setText(webList.getApp_name());
            if (webList.getImage_url().startsWith("http")){
                Glide.with(context).load(webList.getImage_url()).into(((ViewHolder) holder).avatar_url);
            } else {
                Glide.with(context).load("file:///android_asset/"+webList.getImage_url()).into(((ViewHolder) holder).avatar_url);
            }

            ((ViewHolder) holder).avatar_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buka_more = webList.getApp_url();
                    String str = buka_more;
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(str)));
                }
            });
        }

    }

    @Override

    public int getItemCount() {
        return webLists == null ? 0 : webLists.size();
    }

    public More getItem(int position) {
        return webLists.get(position);
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView html_url;
        public ImageView avatar_url;

        public ViewHolder(View itemView) {
            super(itemView);
            html_url = itemView.findViewById(R.id.txtCategory);
            avatar_url = itemView.findViewById(R.id.imgWall);

        }
    }

}
