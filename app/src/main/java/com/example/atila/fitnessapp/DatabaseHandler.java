package com.example.atila.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SidonKK on 09-05-2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_CREATE = "CREATE TABLE " +
            UserData.Info.DATABASE_TABLE + " ("
            + UserData.Info.KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," +
            UserData.Info.NAME + " TEXT NOT NULL," +
            UserData.Info.TIME + " TEXT NOT NULL);";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS");
        onCreate(db);

    }
}
