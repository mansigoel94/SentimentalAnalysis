package com.example.mansi.sentimentalanalysis.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SentimentsContract {


    private SentimentsContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.mansi.sentimentalanalysis";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SENTIMENTS = "sentiments";

    public static final class SentimentsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SENTIMENTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SENTIMENTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SENTIMENTS;

        //review table name and columns
        public final static String REVIEWS_TABLE_NAME = "reviews";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_APP_ID = "app_id";

        public final static String COLUMN_REVIEW = "review_data";

        public final static String COLUMN_RATING = "rating";

        public final static String COLUMN_TIME = "time";

        //app table name and columns
        public final static String APP_TABLE_NAME = "apps";

        public final static String COL_APP_NAME = "app_name";

    }
}

