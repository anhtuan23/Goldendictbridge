package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecyclerView_TextViewHolder extends RecyclerView.ViewHolder {
  public TextView textView;
  public RecyclerView_TextViewHolder(View itemView) {
    super(itemView);
    textView = (TextView) itemView.findViewById(R.id.text);
  }
}