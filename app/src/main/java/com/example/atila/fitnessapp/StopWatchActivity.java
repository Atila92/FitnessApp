package com.example.atila.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StopWatchActivity extends Activity {
    //Settings

TextView pref_text;


    /** Called when the activity is first created. */
    private static final String TAG = "shiiiiiiit";

    private TextView tempTextView; //Temporary TextView
    private Button tempBtn; //Temporary Button
    private Handler mHandler = new Handler();
    private long startTime;
    private long stopTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String hours,minutes,seconds,milliseconds;
    private long secs,mins,hrs,msecs;
    private boolean stopped = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        tempTextView = (TextView) findViewById(R.id.timer);


        Button tempBtn = (Button)findViewById(R.id.startButton);

        tempBtn = (Button)findViewById(R.id.saveButton);

        tempBtn = (Button)findViewById(R.id.resetButton);

        tempBtn = (Button)findViewById(R.id.stopButton);

        pref_text = (TextView) findViewById(R.id.textView);


        loadPref();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent();
        i.setClass(StopWatchActivity.this, SettingsActivity.class);
        startActivityForResult(i, 0);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int result, Intent data){
        loadPref();

    }

    private void loadPref(){

        SharedPreferences myShare = PreferenceManager.getDefaultSharedPreferences(this);
        String name = myShare.getString("pref","");
        pref_text.setText(name);

    }


    public void startClick (View view){
        showStopButton();
        if(stopped){
            startTime = System.currentTimeMillis() - elapsedTime;
        }
        else{
            startTime = System.currentTimeMillis();
        }
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
    }

    public void stopClick (View view){
        hideStopButton();
        mHandler.removeCallbacks(startTimer);
        stopped = true;
    }

    public void resetClick (View view){
        stopped = false;
        ((TextView)findViewById(R.id.timer)).setText("00:00:00");
    }

    public void saveClick (View view){
        Log.i(TAG, "sae knap clicked");
    }

    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this,REFRESH_RATE);
        }
    };

    private void updateTimer (float time){
        secs = (long)(time/1000);
        mins = (long)((time/1000)/60);
        hrs = (long)(((time/1000)/60)/60);

		/* Convert the seconds to String
		 * and format to ensure it has
		 * a leading zero when required
		 */
        secs = secs % 60;
        seconds=String.valueOf(secs);
        if(secs == 0){
            seconds = "00";
        }
        if(secs <10 && secs > 0){
            seconds = "0"+seconds;
        }

		/* Convert the minutes to String and format the String */

        mins = mins % 60;
        minutes=String.valueOf(mins);
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }

    	/* Convert the hours to String and format the String */

        hours=String.valueOf(hrs);
        if(hrs == 0){
            hours = "00";
        }
        if(hrs <10 && hrs > 0){
            hours = "0"+hours;
        }

		/* Setting the timer text to the elapsed time */
        ((TextView)findViewById(R.id.timer)).setText(hours + ":" + minutes + ":" + seconds);

    }

    private void showStopButton(){
        ((Button)findViewById(R.id.startButton)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.resetButton)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.saveButton)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.stopButton)).setVisibility(View.VISIBLE);
    }

    private void hideStopButton(){
        ((Button)findViewById(R.id.startButton)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.resetButton)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.saveButton)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.stopButton)).setVisibility(View.GONE);
    }


}