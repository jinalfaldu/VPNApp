package com.expressvpn.securesafe.AVA_Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.expressvpn.securesafe.AVA_Model.AVA_CountryList;
import com.expressvpn.securesafe.R;

import java.util.ArrayList;

public class AVA_CustomAdapter extends RecyclerView.Adapter<AVA_CustomAdapter.ViewHolder> {
    private ArrayList<AVA_CountryList> localDataSet;
    AVA_ClickListiner mClickListiner;
    Activity mActivity;
    int selcted;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView iv_ivon;
        private final ImageView iv_selected;
        private final FrameLayout banner_container;
        private final RelativeLayout nativead;

        public ViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.tv_server);
            iv_ivon = view.findViewById(R.id.iv_ivon);
            iv_selected = view.findViewById(R.id.iv_selected);
            banner_container = view.findViewById(R.id.banner_container);
            nativead = view.findViewById(R.id.native_ad);
        }

        public TextView getTextView() {
            return textView;
        }
    }
    public AVA_CustomAdapter(Activity mActivity, int selcted, ArrayList<AVA_CountryList> dataSet, AVA_ClickListiner mClickListiner) {
        localDataSet = dataSet;
        this.mActivity = mActivity;
        this.selcted = selcted;
        this.mClickListiner = mClickListiner;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.silentsurfer_text_row_item, viewGroup, false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.silentsurfer_static_80_native_layout, viewGroup, false);
            return new ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position)
    {
        int itemType = getItemViewType(position);
        if (itemType == ITEM_DATA) {
            Glide.with(mActivity).load(localDataSet.get(position).getIcon()).into(viewHolder.iv_ivon);
            viewHolder.textView.setText(localDataSet.get(position).getName());
            viewHolder.itemView.setOnClickListener(v -> {
                try {
                    mClickListiner.getPosition(position, localDataSet.get(position).getCode(), localDataSet.get(position).getName(), localDataSet.get(position).getIcon());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if (selcted == position) {
                viewHolder.iv_selected.setImageResource(R.drawable.ava_ic_select);
            } else {
                viewHolder.iv_selected.setImageResource(R.drawable.ava_ic_unselect);
            }
        }else {

        }
    }
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
    private int ITEM_DATA = 1;
    @Override
    public int getItemViewType(int position) {
        if (localDataSet.get(position) == null) {
            return 0;
        } else {
            return ITEM_DATA;
        }
    }
}
