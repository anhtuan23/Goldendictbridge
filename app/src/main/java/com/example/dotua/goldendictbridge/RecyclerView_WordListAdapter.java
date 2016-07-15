package com.example.dotua.goldendictbridge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.example.dotua.goldendictbridge.Main_Activity.cancelAllAsyncTask;
import static com.example.dotua.goldendictbridge.Main_Activity.executeDirectTranslateImageView;
import static com.example.dotua.goldendictbridge.Main_Activity.executeDirectTranslateTask;
import static com.example.dotua.goldendictbridge.Main_Activity.resetCardViewPosition;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getDesiredString;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getReceivedWord;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.showPopupMenu;

public class RecyclerView_WordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int ITEM_VIEW_TYPE_HEADER = 0;
  private static final int ITEM_VIEW_TYPE_ITEM = 1;

//  private final View header;
  private final List<String> words;
  private final int numberOfCharacter;

  public RecyclerView_WordListAdapter(List<String> words, int numberOfCharacter) {
    this.words = words;
    this.numberOfCharacter = numberOfCharacter;
  }

  public boolean isHeader(int position) {
    return position == 0;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder viewHolder;
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    switch (viewType) {
      case ITEM_VIEW_TYPE_ITEM:
        View v1 = inflater.inflate(R.layout.recycler_view__item, parent, false);
        viewHolder = new RecyclerView_TextViewHolder(v1);
        break;
      default:
        View v2 = inflater.inflate(R.layout.recycler_view__auto_fit_header, parent, false);
        viewHolder = new RecyclerView_CardViewHolder(v2);
        break;
    }
    return viewHolder;

//    if (viewType == ITEM_VIEW_TYPE_HEADER) {
//      return new RecyclerView_TextViewHolder(header);
//    }
//    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view__item, parent, false);
//    return new RecyclerView_TextViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    if (isHeader(position)) {
      RecyclerView_CardViewHolder recyclerView_CardViewHolder =
              (RecyclerView_CardViewHolder)holder;
//      RelativeLayout header = (RelativeLayout)LayoutInflater.from(context).inflate(
//              R.layout.recycler_view__auto_fit_header,
//              recyclerView,
//              false);
      final String receivedWord = getReceivedWord();
      recyclerView_CardViewHolder.headerTextView.setText(receivedWord);
      recyclerView_CardViewHolder.headerTextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//        sendMessage(v.getContext(), sendString,R.integer.action_not_defined);
          cancelAllAsyncTask();
          executeDirectTranslateTask(receivedWord);
          resetCardViewPosition();
        }
      });

      recyclerView_CardViewHolder.headerTextView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          showPopupMenu(v,receivedWord);
          return true;
        }
      });

    } else {

      final String label = words.get(position - 1);  // Subtract 1 for header
      RecyclerView_TextViewHolder recyclerView_textViewHolder = (RecyclerView_TextViewHolder) holder;
      recyclerView_textViewHolder.textView.setText(label);
      final String sendString = getDesiredString(words, numberOfCharacter, position - 1);

      recyclerView_textViewHolder.textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          cancelAllAsyncTask();
          executeDirectTranslateTask(sendString);
          executeDirectTranslateImageView(sendString);
          resetCardViewPosition();
        }
      });

      recyclerView_textViewHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          sendToPopupMenu(v, position);
          return true;
        }
      });
    }
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