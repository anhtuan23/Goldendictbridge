package com.example.dotua.goldendictbridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Created by dotua on 26-Jun-16.
 */
public class Database_InsertTask extends AsyncTask<String, Void, Long> {
    Context context;

    public Database_InsertTask(Context context) {
        this.context = context;
    }

    @Override
    protected Long doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String word = params[0];

        long currentTime = System.currentTimeMillis();
        Database_WordHistoryDbHelper database_wordHistoryDbHelper = new Database_WordHistoryDbHelper(context);
        // Gets the data repository in write mode
        SQLiteDatabase db = database_wordHistoryDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Database_WordHistoryContract.WordHistory.COLUMN_WORD, word);
        values.put(Database_WordHistoryContract.WordHistory.COLUMN_DATE_ADDED, currentTime);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(
                Database_WordHistoryContract.WordHistory.TABLE_NAME,
                null,
                values);
        return  newRowId;
    }

    @Override
    protected void onPostExecute(Long newRowId) {
        super.onPostExecute(newRowId);
        //Toast.makeText(context, newRowId.toString() + " " + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
    }


}
