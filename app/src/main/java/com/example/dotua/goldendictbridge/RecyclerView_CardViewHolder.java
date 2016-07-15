package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by dotua on 11-Jul-16.
 */
public class RecyclerView_CardViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView headerImageView;
    public TextView headerTextView;

    public RecyclerView_CardViewHolder(View v) {
        super(v);
        headerImageView = (SimpleDraweeView) v.findViewById(R.id.headerImage);
        headerTextView = (TextView) v.findViewById(R.id.headerText);
    }
}
