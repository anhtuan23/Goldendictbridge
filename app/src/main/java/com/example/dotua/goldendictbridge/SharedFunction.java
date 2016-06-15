package com.example.dotua.goldendictbridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dotua on 29-May-16.
 */
public class SharedFunction {

    public static MyFragment newMyFragmentInstance(Context context, int numberOfCharacter) {
        MyFragment myFragment = new MyFragment();

        Bundle args = new Bundle();
        args.putInt(context.getString(R.string.bundle_key_number_of_character), numberOfCharacter);
        myFragment.setArguments(args);

        return myFragment;
    }

    public static void sendMessage(Context context, String word) {
        final SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //CAUTION: the switch is not binded to sharedpreference
        boolean b = sharedPref.getBoolean(context.getString(R.string.pref_share_mode_key), false);
        if (b) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, word);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        } else {
            Intent intent = new Intent("colordict.intent.action.SEARCH");
            intent.putExtra("EXTRA_QUERY", word); //Search Query
            context.startActivity(intent);
        }
    }

    public static void lookUpAgain(Context context, View view){
        TextView textView = (TextView)view;
        sendMessage(context, textView.getText().toString());
    }

    public static void executeFragmentWordIntent (final Context context,
                                                  Intent intent,
                                                  View rootView,
                                                  int numberOfCharacter){

        List<String> wordList;
        String receivedWord = "明天更残酷";
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri data = intent.getData();
            List<String> params = data.getPathSegments();
            receivedWord = params.get(0);
            if (receivedWord.equals(""))
                receivedWord = "Bonjour";
        }

        if (Intent.ACTION_SEND.equals(action)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                receivedWord = sharedText;
            } else {
                receivedWord = "Bonjour";
            }
        }

        wordList = new ArrayList<>();

        for (char c : receivedWord.toCharArray()) {
            wordList.add(String.valueOf(c));
        }
        //////////////////////////////////////
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(context));
        recyclerView.setHasFixedSize(true);

        TextView header = (TextView)LayoutInflater.from(context).inflate(R.layout.auto_fit_header, recyclerView, false);
        header.setText(receivedWord);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(context,((TextView)v).getText().toString());
            }
        });
        final WordListAdapter adapter = new WordListAdapter(header, wordList, numberOfCharacter);

        final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });

        recyclerView.setAdapter(adapter);

        if (wordList.size() <= 2 && !wordList.get(0).equals(" ") )
            sendMessage(context, receivedWord);

    }

    public static String getDesiredString (List<String> wordList,int numberOfCharacter, int position){
        String string;
        int wordListSize = wordList.size();
        if (numberOfCharacter == 2 && position < wordListSize-1){
            string = wordList.get(position) + wordList.get(position+1);
        }else if (numberOfCharacter == 3 && position < wordListSize-2){
            string = wordList.get(position) + wordList.get(position+1) + wordList.get(position+2);
        } else {
            string = wordList.get(position);
        }
        return string;
    }
}


