package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.example.dotua.goldendictbridge.Main_Activity.resetCardViewPosition;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getDesiredString;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.showPopupMenu;

public class RecyclerView_WordListAdapter extends RecyclerView.Adapter<RecyclerView_TextViewHolder> {
  private static final int ITEM_VIEW_TYPE_HEADER = 0;
  private static final int ITEM_VIEW_TYPE_ITEM = 1;

  private final View header;
  private final List<String> words;
  private final int numberOfCharacter;

  public RecyclerView_WordListAdapter(View header, List<String> words, int numberOfCharacter) {
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
  public RecyclerView_TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_VIEW_TYPE_HEADER) {
      return new RecyclerView_TextViewHolder(header);
    }
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view__item, parent, false);
    return new RecyclerView_TextViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final RecyclerView_TextViewHolder holder, final int position) {
    if (isHeader(position)) {
      return;
    }
    final String label = words.get(position - 1);  // Subtract 1 for header
    holder.textView.setText(label);
    final String sendString = getDesiredString(words,numberOfCharacter,position-1);

    holder.textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        sendMessage(v.getContext(), sendString,R.integer.action_not_defined);
          DirectTranslate_Task directTranslate_task = new DirectTranslate_Task();
          directTranslate_task.execute(sendString);

          resetCardViewPosition();
      }
    });

    holder.textView.setOnLongClickListener(new View.OnLongClickListener(){
      @Override
      public boolean onLongClick(View v) {
        sendToPopupMenu(v, position);
        return true;
      }
    });
  }

  private void sendToPopupMenu(final View v, final int position){
    String sendString = getDesiredString(words,numberOfCharacter,position-1);
    showPopupMenu(v,sendString);
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