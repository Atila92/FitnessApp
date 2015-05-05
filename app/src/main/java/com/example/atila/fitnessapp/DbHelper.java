package com.example.atila.fitnessapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context) {
        super(context, TimerContract.DB_NAME, null, TimerContract.DB_VERSION);
    }

    // Called only once first time we create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String
                .format("create table %s (%s int primary key, %s text, %s text, %s int)",
                        TimerContract.TABLE, TimerContract.Column.ID,
                        TimerContract.Column.USER,
                        TimerContract.Column.MESSAGE,
                        TimerContract.Column.CREATED_AT);
        Log.d(TAG, "onCreate with SQL: " + sql);
        db.execSQL(sql);
    }

    // Gets called whenever existing version != new version, i.e. schema changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Typically you do ALTER TABLE ...
        db.execSQL("drop table if exists " + TimerContract.TABLE);
        onCreate(db);
    }

}
