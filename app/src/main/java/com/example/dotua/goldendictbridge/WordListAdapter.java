package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.example.dotua.goldendictbridge.SharedFunction.getDesiredString;
import static com.example.dotua.goldendictbridge.SharedFunction.sendMessage;

public class WordListAdapter extends RecyclerView.Adapter<TextViewHolder> {
  private static final int ITEM_VIEW_TYPE_HEADER = 0;
  private static final int ITEM_VIEW_TYPE_ITEM = 1;

  private final View header;
  private final List<String> words;
  private final int numberOfCharacter;

  public WordListAdapter(View header, List<String> words, int numberOfCharacter) {
    if (header == null) {
      throw new IllegalArgumentException("header may not be null");
    }
    this.header = header;
    this.words = words;
    this.numberOfCharacter = numberOfCharacter;
  }

  public boolean isHeader(int position) {
    return position == 0;
  }

  @Override
  public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_VIEW_TYPE_HEADER) {
      return new TextViewHolder(header);
    }
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    return new TextViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final TextViewHolder holder, final int position) {
    if (isHeader(position)) {
      return;
    }
    final String label = words.get(position - 1);  // Subtract 1 for header
    holder.textView.setText(label);


    holder.textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String sendString = getDesiredString(words,numberOfCharacter,position-1);
        sendMessage(v.getContext(),sendString);
      }
    });
  }

  @Override
  public int getItemViewType(int position) {
    return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
  }

  @Override
  public int getItemCount() {
    return words.size() + 1;
  }
}