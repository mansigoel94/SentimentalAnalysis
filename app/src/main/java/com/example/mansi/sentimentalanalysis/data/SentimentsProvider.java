package com.example.mansi.sentimentalanalysis.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class SentimentsProvider extends ContentProvider {

    private static final String TAG = "SentimentsProvider";

    private static final int SENTIMENTS = 100;
    private static final int SENTIMENTS_ID = 101;

    private SentimentsDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(SentimentsContract.CONTENT_AUTHORITY,
                SentimentsContract.PATH_SENTIMENTS, SENTIMENTS);
        sUriMatcher.addURI(SentimentsContract.CONTENT_AUTHORITY,
                SentimentsContract.PATH_SENTIMENTS + "/#", SENTIMENTS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new SentimentsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SENTIMENTS:
                cursor = database.query(SentimentsContract.SentimentsEntry.REVIEWS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case SENTIMENTS_ID:
                selection = SentimentsContract.SentimentsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(SentimentsContract.SentimentsEntry.REVIEWS_TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SENTIMENTS:
                return insertReview(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertReview(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(SentimentsContract.SentimentsEntry.REVIEWS_TABLE_NAME,
                null, values);
        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    //update method would not be used since this app does not support editing of reviews
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SENTIMENTS:
                return 0;
            case SENTIMENTS_ID:
                selection = SentimentsContract.SentimentsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return 0;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    //delete method would not be used since this app does not support deleting of reviews
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SENTIMENTS:
                rowsDeleted = database.delete(
                        SentimentsContract.SentimentsEntry.REVIEWS_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case SENTIMENTS_ID:
                selection = SentimentsContract.SentimentsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(
                        SentimentsContract.SentimentsEntry.REVIEWS_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SENTIMENTS:
                return SentimentsContract.SentimentsEntry.CONTENT_LIST_TYPE;
            case SENTIMENTS_ID:
                return SentimentsContract.SentimentsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
