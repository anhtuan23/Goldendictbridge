package com.example.dotua.goldendictbridge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

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
    final String sendString = getDesiredString(words,numberOfCharacter,position-1);

    holder.textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendMessage(v.getContext(), sendString);
      }
    });

    holder.textView.setOnLongClickListener(new View.OnLongClickListener(){
      @Override
      public boolean onLongClick(View v) {
        showPopupMenu(v, position);
        return true;
      }
    });
  }

  private void showPopupMenu (final View v, final int position){
    //Creating the instance of PopupMenu
    PopupMenu popup = new PopupMenu(v.getContext(), v);
    //Inflating the Popup using xml file
    popup.getMenuInflater()
            .inflate(R.menu.popup_menu, popup.getMenu());

    //registering popup with OnMenuItemClickListener
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        String sendString = getDesiredString(words,numberOfCharacter,position-1);
        String baseurl;
        Uri builtUri = Uri.parse(("https://www.google.com"));
        Intent intent;
        switch (item.getItemId()) {
          case R.id.bing_dict:
            baseurl = "http://www.bing.com/dict/search?mkt=zh-CN&setlang=ZH";
            builtUri = Uri.parse(baseurl).buildUpon().appendQueryParameter("q", sendString).build();
            break;
          case R.id.character_pop:
            baseurl = "https://characterpop.com/explode";
            builtUri = Uri.parse(baseurl).buildUpon().appendPath(sendString).build();
            break;
          case R.id.image:
            baseurl = "https://www.google.com/search?tbm=isch";
            builtUri = Uri.parse(baseurl).buildUpon().appendQueryParameter("q", sendString).build();
            break;
          default:
            break;

        }
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(builtUri);
        v.getContext().startActivity(intent);
        return  true;
      }
    });

    popup.show(); //showing popup menu
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