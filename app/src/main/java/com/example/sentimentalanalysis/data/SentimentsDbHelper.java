package com.example.sentimentalanalysis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SentimentsDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sentimentsAnalysis.db";

    private static final int DATABASE_VERSION = 1;

    String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " +
            SentimentsContract.SentimentsEntry.REVIEWS_TABLE_NAME + " ("
            + SentimentsContract.SentimentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SentimentsContract.SentimentsEntry.COLUMN_APP_ID + " INTEGER NOT NULL, "
            + SentimentsContract.SentimentsEntry.COLUMN_REVIEW + " TEXT, "
            + SentimentsContract.SentimentsEntry.COLUMN_RATING + " REAL, "
            + SentimentsContract.SentimentsEntry.COLUMN_TIME + " INTEGER NOT NULL);";

    String SQL_CREATE_APP_TABLE = "CREATE TABLE " +
            SentimentsContract.SentimentsEntry.APP_TABLE_NAME + " (" +
            SentimentsContract.SentimentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SentimentsContract.SentimentsEntry.COL_APP_NAME + " TEXT NOT NULL);";


    public SentimentsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
        db.execSQL(SQL_CREATE_APP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}