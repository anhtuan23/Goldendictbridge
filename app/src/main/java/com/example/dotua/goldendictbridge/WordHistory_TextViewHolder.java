package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dotua on 26-Jun-16.
 */
public class WordHistory_TextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public WordHistory_TextViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.text);
    }
}
