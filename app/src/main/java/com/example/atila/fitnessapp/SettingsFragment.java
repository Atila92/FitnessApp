package com.example.atila.fitnessapp;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by Atila on 20-04-2015.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences prefs;

@Override
public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);

}

@Override
public void onStart(){
super.onStart();
    prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    prefs.registerOnSharedPreferenceChangeListener(this);
}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}


