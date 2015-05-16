package com.example.atila.fitnessapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

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
        loadPref();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    uploadData();
                    cursor.requery();

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
        deleteDatabaseRows();
        cursor.close();
    }
    private void loadPref(){

        SharedPreferences myShare = PreferenceManager.getDefaultSharedPreferences(this);
        name = myShare.getString("pref","");

    }

    private Cursor getData(){
        DatabaseHandler dbHandler = new DatabaseHandler(this, UserData.Info.DATABASE_NAME, null,UserData.Info.DATABASE_VERSION);
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String[] projection = {UserData.Info.NAME
                , UserData.Info.TIME};
        Cursor cursor  = db.query(UserData.Info.DATABASE_TABLE, projection,null,null,null,null,null);

        return cursor;
    }
    private void deleteDatabaseRows(){
        DatabaseHandler dbHandler = new DatabaseHandler(this, UserData.Info.DATABASE_NAME, null,UserData.Info.DATABASE_VERSION);
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        db.execSQL("DELETE FROM " + UserData.Info.DATABASE_TABLE);


    }




    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


}
