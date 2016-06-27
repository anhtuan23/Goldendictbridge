package com.example.dotua.goldendictbridge;

import android.provider.BaseColumns;

/**
 * Created by dotua on 26-Jun-16.
 */
public final class Database_WordHistoryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Database_WordHistoryContract() {}

    /* Inner class that defines the table contents */
    public static abstract class WordHistory implements BaseColumns {
        public static final String TABLE_NAME = "word_history";
        public static final String COLUMN_WORD = "title";
        public static final String COLUMN_DATE_ADDED = "date_added";


    }
}
