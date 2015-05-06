package com.example.atila.fitnessapp;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Atila on 06-05-2015.
 */
public class SettingFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//load the preferences from an XML Resource
        addPreferencesFromResource(R.xml.settings);
    }

}
