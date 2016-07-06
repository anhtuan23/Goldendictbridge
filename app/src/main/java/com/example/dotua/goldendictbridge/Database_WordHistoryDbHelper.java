package com.example.dotua.goldendictbridge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dotua.goldendictbridge.Database_WordHistoryContract.WordHistory;

/**
 * Created by dotua on 26-Jun-16.
 */
public class Database_WordHistoryDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WordHistory.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WordHistory.TABLE_NAME + " (" +
                    WordHistory._ID + " INTEGER PRIMARY KEY," +
                    WordHistory.COLUMN_WORD + TEXT_TYPE + COMMA_SEP +
                    WordHistory.COLUMN_DATE_ADDED + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WordHistory.TABLE_NAME;

    public Database_WordHistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void executeInsertTask(Context context, String receivedWord){
        Database_InsertTask insertTask = new Database_InsertTask(context);
        insertTask.execute(receivedWord);
    }

    public static void executeQueryTask(Context context, WordHistory_Adapter mAdapter){
        Database_QueryTask queryTask = new Database_QueryTask(context);
        queryTask.execute(mAdapter);
    }
}