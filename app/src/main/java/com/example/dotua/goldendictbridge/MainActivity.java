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

    String word = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();



        if (Intent.ACTION_VIEW.equals(action)) {
            //if ("text/plain".equals(type)) {
            Uri data = intent.getData();
            List<String> params = data.getPathSegments();
            word = params.get(0);
            TextView wordView = (TextView) findViewById(R.id.hello);
            wordView.setText(word);
            sendMessage(word);
        }

    }

    public void sendMessage(String word) {
        Intent intent = new Intent("colordict.intent.action.SEARCH");
        intent.putExtra("EXTRA_QUERY", word); //Search Query
        //String GD = intent.toUri(0x3000000);
        startActivity(intent);
    }

    public void lookUpAgain(View view){
        //String word = textView.getText().toString();
        sendMessage(word);
    }

}
