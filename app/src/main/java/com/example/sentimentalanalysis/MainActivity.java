package com.example.sentimentalanalysis;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.sentimentalanalysis.adapter.AppAdapter;
import com.example.sentimentalanalysis.data.SentimentsContract;
import com.example.sentimentalanalysis.data.SentimentsDbHelper;
import com.example.sentimentalanalysis.model.App;
import com.example.sentimentalanalysis.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String APP_KEY = "app_key";
    public static final String APP_ID = "app_pos";
    @BindView(R.id.rv_apps)
    RecyclerView rvApps;
    @BindView(R.id.searchview)

    SearchView searchview;
    private AppAdapter adapter;
    private static final String TAG = "MainActivity";
    private SentimentsDbHelper dbHelper;
    private SQLiteDatabase writeDatabase;
    private SQLiteDatabase readDatabase;
    private ArrayList<App> appArrayList;
    private ArrayList<App> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dbHelper = new SentimentsDbHelper(this);
        writeDatabase = dbHelper.getWritableDatabase();
        readDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = readDatabase.query(SentimentsContract.SentimentsEntry.APP_TABLE_NAME,
                null, SentimentsContract.SentimentsEntry.COL_APP_NAME + "=?",
                new String[]{"Netflix"}, null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);

        appArrayList = new ArrayList<>();
/*        appArrayList.add(new App(R.drawable.ic_netflix,
                "Netflix",
                "Software Company", "4.5", "5.4 MB",
                null));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Whatsapp",
                "Software Company", "4.5", "5.4 MB",
                null));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Instagram",
                "Software Company", "4.5", "5.4 MB",
                null));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Facebook",
                "Software Company", "4.5", "5.4 MB",
                null));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Rahul tinder",
                "Software Company", "4.5", "5.4 MB",
                null));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Amazon",
                "Software Company", "4.5", "5.4 MB",
                null));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Popers",
                "Software Company", "4.5", "5.4 MB",
                null));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Myntra",
                "Software Company", "4.5", "5.4 MB",
                null));*/

        appArrayList.add(new App(R.drawable.ic_netflix,
                "Netflix",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Whatsapp",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Instagram",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Facebook",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Rahul tinder",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Amazon",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Popers",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));
        appArrayList.add(new App(R.drawable.ic_netflix,
                "Myntra",
                "Software Company", "4.5", "5.4 MB",
                fetchReviewsFromDatabase(id++)));

        updateSentimentalAnalysisValue(appArrayList);

//        deleteAllAppsFromDatabase();
//        insertAppIntoDatabase(appArrayList);

        filterList = new ArrayList<App>(appArrayList);

        AppAdapter.ItemClickListener itemClickListener = new AppAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int app_id;
                Cursor cursor = readDatabase.query(
                        SentimentsContract.SentimentsEntry.APP_TABLE_NAME,
                        null,
                        SentimentsContract.SentimentsEntry.COL_APP_NAME + "=?",
                        new String[]{filterList.get(position).getName()},
                        null,
                        null,
                        null);

                cursor.moveToFirst();
                app_id = cursor.getInt(0);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(APP_KEY, filterList.get(position));
                intent.putExtra(APP_ID, app_id);
                startActivity(intent);
            }
        };

        adapter = new AppAdapter(filterList, itemClickListener, this);
        rvApps.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        rvApps.setAdapter(adapter);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList.clear();
                s = s.toLowerCase();
                for (App app : appArrayList) {
//                    Log.d(TAG, "onQueryTextChange: app name "+app.getName());
//                    Log.d(TAG, "onQueryTextChange: s "+s);
                    if (app.getName().toLowerCase().contains(s)) {
                        //   Log.d(TAG, "onQueryTextChange: if condition");
                        filterList.add(app);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });


/*        searchview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d(TAG, "onFocusChange: " + b);
            }
        });

        searchview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "onClose() called");
                return false;
            }
        });

        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchview.hasFocus()) {
                    Log.d(TAG, "onClick: focus");
                } else {
                    Log.d(TAG, "onClick: focus lost");
                }
            }
        });*/

        //todo close keyboard on click of close button of searchview
        // Catch event on [x] button inside search view
        /*
        int searchCloseButtonId = searchview.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        final ImageView closeButton = (ImageView) this.searchview.findViewById(searchCloseButtonId);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchview.clearFocus();
                hideKeyboard();
                closeButton.setVisibility(View.GONE);
            }
        });
        */

    }

    private void deleteAllAppsFromDatabase() {
        writeDatabase.delete(SentimentsContract.SentimentsEntry.APP_TABLE_NAME,
                null, null);
    }

    private void insertAppIntoDatabase(ArrayList<App> appArrayList) {
        for (App app : appArrayList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SentimentsContract.SentimentsEntry.COL_APP_NAME,
                    app.getName());
            writeDatabase.insert(SentimentsContract.SentimentsEntry.APP_TABLE_NAME,
                    null, contentValues);
        }
    }

    public ArrayList<Review> fetchReviewsFromDatabase(int app_id) {
        Cursor cursor = readDatabase.query(
                SentimentsContract.SentimentsEntry.REVIEWS_TABLE_NAME,
                null,
                SentimentsContract.SentimentsEntry.COLUMN_APP_ID + "=?",
                new String[]{String.valueOf(app_id)},
                null, null, null);

        ArrayList<Review> reviewArrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            reviewArrayList.add(new Review(cursor.getString(2),
                    cursor.getFloat(3),
                    cursor.getLong(4)));
        }
        Log.d(TAG, "fetchReviewsFromDatabase: reviews size " + reviewArrayList.size());
        return reviewArrayList;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_all:
                filterApps(0);
                break;
            case R.id.menu_fraud:
                filterApps(1);
                break;
            case R.id.menu_fair:
                filterApps(2);
                break;
        }
        return true;
    }

    private void filterApps(int type) {
        switch (type) {
            case 0:
                Log.d(TAG, "filterApps: all " + appArrayList.size());
                filterList.clear();
                filterList.addAll(appArrayList);
                adapter.notifyDataSetChanged();
                searchview.setVisibility(View.VISIBLE);
                break;
            case 1:
                filterList.clear();
                for (App app : appArrayList) {
                    if (!app.isPositive()) {
                        filterList.add(app);
                    }
                }
                Log.d(TAG, "filterApps: neg filter " + filterList.size());
                adapter.notifyDataSetChanged();
                searchview.setVisibility(View.GONE);
                break;
            case 2:
                filterList.clear();
                for (App app : appArrayList) {
                    if (app.isPositive()) {
                        filterList.add(app);
                    }
                }
                Log.d(TAG, "filterApps: positive " + filterList.size());
                adapter.notifyDataSetChanged();
                searchview.setVisibility(View.GONE);
                break;
        }
    }

    public void updateSentimentalAnalysisValue(ArrayList<App> appArrayList) {
//        ArrayList<App> positiveApps = new ArrayList<>();
//        ArrayList<App> negativeApps = new ArrayList<>();
        int positiveCount = 0;
        int negativeCount = 0;

        for (App app : appArrayList) {
            for (Review review : app.getReviewArrayList()) {
                //searching for positive words in single review
                for (String positive : Constants.positiveWords) {
                    if (review.getReview().toLowerCase().contains(positive)) {
                        positiveCount++;
                    }
                }

                //searching for negative words in single review
                for (String negative : Constants.negativeWords) {
                    if (review.getReview().toLowerCase().contains(negative)) {
                        negativeCount++;
                    }
                }
            }
            //for single app we got count of positive and negative words,
            //now lets calculate its sentimental value percentage

            //=because neutral apps will be considered as positive
            if (positiveCount == 0 && negativeCount == 0) {
                Log.d(TAG, "filterApps: " + app.getName() + " positive percentage= 0***");
                app.setIspositive(true);
                app.setSentiValue(0);
//                positiveApps.add(app);
            } else if (positiveCount >= negativeCount) {
                Log.d(TAG, "filterApps: " + app.getName() + " positive percentage= "
                        + (float) (positiveCount - negativeCount) / (float) (positiveCount + negativeCount) * 100);
                float sentiValue = (float) (positiveCount - negativeCount) / (float) (positiveCount + negativeCount) * 100;
                app.setIspositive(true);
                app.setSentiValue(sentiValue);
//                positiveApps.add(app);

            } else {
                Log.d(TAG, "filterApps: " + app.getName() + " negative percentage= "
                        + (float) (negativeCount - positiveCount) / (float) (positiveCount + negativeCount) * 100);
                float sentiValue = (float) (negativeCount - positiveCount) / (float) (positiveCount + negativeCount) * 100;
                app.setIspositive(false);
                app.setSentiValue(sentiValue);
//                negativeApps.add(app);
            }
            positiveCount = 0;
            negativeCount = 0;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
