package com.example.atila.fitnessapp;

import android.app.Activity;
import android.preference.EditTextPreference;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
       getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
    }

}
