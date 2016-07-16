package com.example.dotua.goldendictbridge;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.dotua.goldendictbridge.Database_WordHistoryContract.WordHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dotua on 26-Jun-16.
 */
public class Database_QueryTask extends AsyncTask<WordHistory_Adapter, Void, List<String>> {
    Context context;
    WordHistory_Adapter mAdapter;

    public Database_QueryTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<String> doInBackground(WordHistory_Adapter... params) {
        if (params.length == 0) {
            return null;
        }
        mAdapter = params[0];

        Database_WordHistoryDbHelper database_wordHistoryDbHelper =
                new Database_WordHistoryDbHelper(context);
        SQLiteDatabase db = database_wordHistoryDbHelper.getReadableDatabase();

        String[] projection = {
                WordHistory.COLUMN_WORD,
                WordHistory.COLUMN_DATE_ADDED,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                WordHistory._ID + " DESC";
        String limit = "50";

        Cursor cursor = db.query(
                WordHistory.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                 // The sort order
                limit
        );

        List<String> listString = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                listString.add(cursor.getString(
                        cursor.getColumnIndexOrThrow(WordHistory.COLUMN_WORD)));
            } while (cursor.moveToNext());
        }

        return listString;
    }

    @Override
    protected void onPostExecute(List<String> listString) {
        super.onPostExecute(listString);
        mAdapter.updateDataSet(listString);
        mAdapter.notifyDataSetChanged();
    }


}
