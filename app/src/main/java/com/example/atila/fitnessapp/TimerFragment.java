package com.example.atila.fitnessapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class TimerFragment extends Fragment {
private static final String Tag = TimerFragment.class.getSimpleName();
    private Button mButtonSend;
    private TextView mTextView;

    public View onCreateViewView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate("Timer layout here", null,false);

        //do button stuf here

        //her skal laves knap som skal poste tid på db


return v;
    }

    class PostTime extends AsyncTask <String, Void, String>{
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Posting",
                    "Please wait...");
            progress.setCancelable(true);
        }

        // Executes on a non-UI thread
        @Override
        protected String doInBackground(String... params) {
            try {
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String username = prefs.getString("name", "");
                String email = prefs.getString("email","");
                String password = prefs.getString("password", "");

                // Check that username and password are not empty
                // If empty, Toast a message to set login info and bounce to
                // SettingActivity
                // Hint: TextUtils.
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    getActivity().startActivity(
                            new Intent(getActivity(), SettingsActivity.class));
                    return "Please update your username and password or Email";
                }

               //dogooglestuffhere

                Log.d(TAG, "Successfully posted to the cloud: " + params[0]);
                return "Successfully posted";
            } catch (Exception e) {
                Log.e(TAG, "Failed to post to the cloud", e);
                e.printStackTrace();
                return "Failed to post";
            }
        }

        // Called after doInBackground() on UI thread
        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (getActivity() != null && result != null)
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        }

    }
}
