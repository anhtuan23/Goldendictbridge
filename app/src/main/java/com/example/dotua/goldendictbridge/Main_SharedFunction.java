package com.example.dotua.goldendictbridge;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

public class Main_SharedFunction {
    private static List<String> wordList;
    private static String receivedWord = "明天更残酷";

    public static List<String> getWordList() {return wordList;}
    public static void setWordList(List<String> wordList) {Main_SharedFunction.wordList = wordList;}
    public static String getReceivedWord() {return receivedWord;}
    public static void setReceivedWord(String receivedWord) {Main_SharedFunction.receivedWord = receivedWord;}

    public static void sendMessage(Context context, String word) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.pref_latest_sent_string), word);
        editor.apply();


        //CAUTION: the switch is not bind to sharedPreference
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

    public static int generateWordList(Context context, Intent intent){
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri data = intent.getData();
            List<String> params = data.getPathSegments();
            receivedWord = params.get(0);
            if (receivedWord.equals("")) {
                receivedWord = "Bonjour";
            } else {
                Database_InsertTask insertTask = new Database_InsertTask(context);
                insertTask.execute(receivedWord);
            }
        } else if (Intent.ACTION_SEND.equals(action)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                receivedWord = sharedText;
                Database_InsertTask insertTask = new Database_InsertTask(context);
                insertTask.execute(receivedWord);
            } else {
                receivedWord = "Bonjour";
            }
        } else if (Intent.ACTION_SEARCH.equals(action)) {
            String sharedText = intent.getStringExtra(SearchManager.QUERY);
            if (sharedText != null) {
                receivedWord = sharedText;
                Database_InsertTask insertTask = new Database_InsertTask(context);
                insertTask.execute(receivedWord);
            } else {
                receivedWord = "Bonjour";
            }
        } else if (Intent.ACTION_MAIN.equals(action)){

        }

        wordList = new ArrayList<>();

        for (char c : receivedWord.toCharArray()) {
            wordList.add(String.valueOf(c));
        }

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String latestEntry = sharedPref.getString(context.getString(R.string.pref_latest_sent_string), "Bonjour");

        if (!receivedWord.equals(latestEntry) &&
                wordList.size() <= 2 &&
                !wordList.get(0).equals(" ") ) {
            sendMessage(context, receivedWord);
        }
        return 1;
    }

    public static void showPopupMenu (final View v, final String sendString){
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String baseUrl;
                Uri builtUri;
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.bing_dict:
                        baseUrl = "http://www.bing.com/dict/search?mkt=zh-CN&setlang=ZH";
                        builtUri = Uri.parse(baseUrl).buildUpon().appendQueryParameter("q", sendString).build();
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(builtUri);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.character_pop:
                        baseUrl = "https://characterpop.com/explode";
                        builtUri = Uri.parse(baseUrl).buildUpon().appendPath(sendString).build();
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(builtUri);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.image:
                        baseUrl = "https://www.google.com/search?tbm=isch";
                        builtUri = Uri.parse(baseUrl).buildUpon().appendQueryParameter("q", sendString).build();
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(builtUri);
                        v.getContext().startActivity(intent);
                        break;
                    case  R.id.send_to_search_box:
                        Main_Activity.updateSearchViewQuery(sendString);
                        break;
                    default:
                        break;

                }

                return  true;
            }
        });

        popup.show(); //showing popup menu
    }

    public static String getDesiredString (List<String> wordList,int numberOfCharacter, int position){
        String string;
        int wordListSize = wordList.size();
        if (numberOfCharacter == 2 && position < wordListSize-1){
            string = wordList.get(position) +
                    wordList.get(position+1);
        }else if (numberOfCharacter == 3 && position < wordListSize-2){
            string = wordList.get(position) +
                    wordList.get(position+1) +
                    wordList.get(position+2);
        } else if (numberOfCharacter == 4 && position < wordListSize-3){
            string = wordList.get(position) +
                    wordList.get(position+1) +
                    wordList.get(position+2) +
                    wordList.get(position+3);
        }
        else {
            string = wordList.get(position);
        }
        return string;
    }


}


