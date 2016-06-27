package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dotua on 26-Jun-16.
 */
public class WordHistory_Adapter extends RecyclerView.Adapter<WordHistory_Adapter.ViewHolder> {
    private List<String> mDataset;
    public final static String EXTRA_MESSAGE = "com.example.dotua.goldendictbridge.WORD_HISTORY_MESSAGE";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WordHistory_Adapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WordHistory_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        TextView v = (TextView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_history__textview_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String holderText = mDataset.get(position);
        holder.mTextView.setText(holderText);

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_Activity.updateSearchViewQuery(holderText);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateDataSet(List<String> newList){
        mDataset = newList;
    }
}
