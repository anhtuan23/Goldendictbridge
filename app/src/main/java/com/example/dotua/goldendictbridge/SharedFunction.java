package com.example.dotua.goldendictbridge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dotua on 29-May-16.
 */
public class SharedFunction {


    public static void sendMessage(Context context, String word) {
        Intent intent = new Intent("colordict.intent.action.SEARCH");
        intent.putExtra("EXTRA_QUERY", word); //Search Query
        context.startActivity(intent);
    }

    public static void lookUpAgain(Context context, View view){
        TextView textView = (TextView)view;
        sendMessage(context, textView.getText().toString());
    }

    public static void executeFragmentWordIntent (final Context context,
                                                  Intent intent,
                                                  View rootView,
                                                  int characterNumber){

        ArrayList<String> wordList;
        ArrayList<String> combinedWordList;
        ArrayAdapter<String> wordlistAdapter;
        String receivedWord = "Hi";


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
        wordList.add(receivedWord);
        combinedWordList = new ArrayList<>();
        combinedWordList.add(receivedWord);

        for (char c : receivedWord.toCharArray()) {
            wordList.add(String.valueOf(c));
        }

        int wordListSize = wordList.size();

        if (characterNumber == 1){
            combinedWordList = wordList;
        }else if (characterNumber == 2){
            wordList.add("");
            for (int i = 1; i < wordListSize; i++)
                combinedWordList.add(wordList.get(i) + wordList.get(i+1));
        }else if (characterNumber == 3){
            wordList.add("");
            wordList.add("");
            for (int i = 1; i < wordListSize; i++)
                combinedWordList.add(wordList.get(i) + wordList.get(i+1) + wordList.get(i+2));
        }


        wordlistAdapter = new ArrayAdapter<String>(context,
                R.layout.list_item_word,
                R.id.list_item_word,
                combinedWordList);

        ListView listViewWords = (ListView)rootView.findViewById(R.id.listview_words);
        listViewWords.setAdapter(wordlistAdapter);

        if (wordListSize <= 2 && !wordList.get(0).equals(" ") )
            sendMessage(context, receivedWord);

        listViewWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                lookUpAgain(context, view);
            }
        });
    }
}