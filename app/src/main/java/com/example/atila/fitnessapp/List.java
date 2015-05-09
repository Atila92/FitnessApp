package com.example.atila.fitnessapp;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

/**
 * Created by SidonKK on 09-05-2015.
 */
public class List extends ListActivity {
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cursor = getData();

        String[] columns = {UserData.Info.NAME, UserData.Info.TIME};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, columns, to, 0);
        setListAdapter(listAdapter);
    }

    private  Cursor getData(){
        DatabaseHandler dbHandler = new DatabaseHandler(this, UserData.Info.DATABASE_NAME, null,UserData.Info.DATABASE_VERSION);
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String[] projection = {UserData.Info.KEY_ID, UserData.Info.NAME
                , UserData.Info.TIME};
        Cursor cursor = db.query(UserData.Info.DATABASE_TABLE, projection,null,null,null,null,null);

        return cursor;
    }

}
