package com.example.atila.fitnessapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class Updater extends Service {

    private String name;
    private String time;

    private static final String TAG = "com.example.atila.fitnessapp";
    private static final String URL = "http://toiletgamez.com/fitnessapp_db/save_time.php";
    public static Cursor cursor;

    public class LocalBinder extends Binder {
        Updater getService() {
            return Updater.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    JSONParser jsonParser = new JSONParser();
    @Override
    public void onCreate() {
        //service that checks if there is network on the phone if so it takes data from SQLite DB and post it into MySQL online DB
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while(isNetworkAvailable()) {
                        uploadData();
                        cursor.requery();
                        try {
                            Thread.sleep(5000);


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                            Thread.sleep(5000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }



        };
        new Thread(runnable).start();

    }

    private void uploadData() {
        ///looping the cursor to get data from SQL and post it to MySQL DB
        getData();
        cursor = getData();
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            name = cursor.getString(0);
            time = cursor.getString(1);

            java.util.List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("user_time", time));
            JSONObject json = jsonParser.makeHttpRequest(
                    URL, "POST", params);
            cursor.moveToNext();
        }
        //deleting the data in the SQLite DB after data has been posted
        deleteDatabaseRows();
        cursor.close();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // test for connection
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private Cursor getData(){
        String[] projection = {UserData.Info.NAME
                , UserData.Info.TIME};
        Cursor cursor = getContentResolver().query(ContentProvider.CONTENT_URI,projection,null,null,null);

        return cursor;
    }
    private void deleteDatabaseRows(){

        int count = getContentResolver().delete(ContentProvider.CONTENT_URI,null,null);
       /* DatabaseHandler dbHandler = new DatabaseHandler(this, UserData.Info.DATABASE_NAME, null,UserData.Info.DATABASE_VERSION);
        //and a SQLiteDatabase
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("DELETE FROM " + UserData.Info.DATABASE_TABLE);*/


    }

    @Override
    public void onDestroy()
    {
        stopService(new Intent(this, Updater.class));
    }

}
