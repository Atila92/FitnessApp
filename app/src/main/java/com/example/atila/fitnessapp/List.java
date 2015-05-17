package com.example.atila.fitnessapp;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SidonKK on 09-05-2015.
 */
public class List extends ListActivity {

    private static final String READ_TIME_URL = "http://toiletgamez.com/fitnessapp_db/get_time.php";

    //JSON IDS used for the database:
    private static final String TAG_POSTS = "posts";


    private JSONArray mTimes = null;


    private ArrayList<HashMap<String, String>> mTimeList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list);


    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the times via AsyncTask
        new LoadTimes().execute();

    }


    public void updateJSONdata() {

        mTimeList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFromUrl(READ_TIME_URL);

        try {

            //available

            mTimes = json.getJSONArray(TAG_POSTS);



            // looping through all times according to the json object returned

            for (int i = 0; i < mTimes.length(); i++) {

                JSONObject c = mTimes.getJSONObject(i);



                //gets the content of each tag

                String name = c.getString("Name");

                String time = c.getString("Time");


                // creating new HashMap

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("Name", name);

                map.put("Time", time);

                // adding HashList to ArrayList

                mTimeList.add(map);

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

    }


    private void updateList() {

        int[] to = {android.R.id.text1, android.R.id.text2};
        String[] columns = {"Name", "Time"};
        //using listadapter til put the data from array to the list
        ListAdapter adapter = new SimpleAdapter(this, mTimeList,

                android.R.layout.simple_list_item_2, columns, to);


        setListAdapter(adapter);


    }
    public class LoadTimes extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            updateList();

        }

    }




}
