package com.example.dotua.goldendictbridge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String words = " ";
    String[] word = {" ", " ", " ", " ", " "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri data = intent.getData();
            List<String> params = data.getPathSegments();
            words = params.get(0);

            int i = 0;
            for (char c : words.toCharArray()) {
                word[i] = String.valueOf(c);
                i++;
            }

            TextView wordView0 = (TextView) findViewById(R.id.hello0);
            wordView0.setText(words);
            TextView wordView1 = (TextView) findViewById(R.id.hello1);
            wordView1.setText(word[0]);
            TextView wordView2 = (TextView) findViewById(R.id.hello2);
            wordView2.setText(word[1]);

            if (word[1].equals(" "))
                sendMessage(words);
        }

    }

    public void sendMessage(String word) {
        Intent intent = new Intent("colordict.intent.action.SEARCH");
        intent.putExtra("EXTRA_QUERY", word); //Search Query
        startActivity(intent);
    }

    public void lookUpAgain0(View view){
        sendMessage(words);
    }

    public void lookUpAgain1(View view){
        sendMessage(word[0]);
    }

    public void lookUpAgain2(View view) {
        sendMessage(word[1]);
    }
}
