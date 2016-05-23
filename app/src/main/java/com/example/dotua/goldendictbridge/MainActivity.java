package com.example.dotua.goldendictbridge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> wordList;
    ArrayAdapter<String> wordlistAdapter;
    String receivedWord = "Hi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri data = intent.getData();
            List<String> params = data.getPathSegments();
            receivedWord = params.get(0);
            if (receivedWord.equals(""))
                receivedWord = "Bonjour";
        }
        wordList = new ArrayList<String>();
        wordList.add(receivedWord);

        for (char c : receivedWord.toCharArray()) {
            wordList.add(String.valueOf(c));
        }

        wordlistAdapter = new ArrayAdapter<String>(this,
                R.layout.list_item_word,
                R.id.list_item_word,
                wordList);

        ListView listViewWords = (ListView)this.findViewById(R.id.listview_words);
        listViewWords.setAdapter(wordlistAdapter);

        if (wordList.size() <= 2 && !wordList.get(0).equals(" ") )
            sendMessage(receivedWord);
    }

    public void sendMessage(String word) {
        Intent intent = new Intent("colordict.intent.action.SEARCH");
        intent.putExtra("EXTRA_QUERY", word); //Search Query
        startActivity(intent);
    }

    public void lookUpAgain(View view){
        TextView textView = (TextView)view;
        sendMessage(textView.getText().toString());
    }
}
