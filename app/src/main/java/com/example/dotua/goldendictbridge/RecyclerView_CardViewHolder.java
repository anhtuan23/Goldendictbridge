package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dotua on 11-Jul-16.
 */
public class RecyclerView_CardViewHolder extends RecyclerView.ViewHolder {

    public ImageView headerImageView;
    public TextView headerTextView;

    public RecyclerView_CardViewHolder(View v) {
        super(v);
        headerImageView = (ImageView) v.findViewById(R.id.header_image);
        headerTextView = (TextView) v.findViewById(R.id.headerText);
    }
}
