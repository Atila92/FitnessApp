package com.example.atila.fitnessapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by andb on 23-02-2015.
 */
public class GoogleCommunicator {

    //Logging
    private static final Boolean D = true;
    private static final String TAG = "GoogleComThread";

    //Spreadsheet communication
    private static final String mScope = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://spreadsheets.google.com/feeds https://docs.google.com/feeds";
    private String mEmail;
    private MainActivity mActivity;
    private String mToken;
    private SpreadsheetService mSpreadSheetService;
    private SpreadsheetFeed mFeed;

    private String mSpreadsheetName;
    private String mWorksheetName;

    private SpreadsheetEntry mSpreadsheet;
    private WorksheetEntry mWorksheet;


    //Constructor
    public GoogleCommunicator(MainActivity activity, String email) {
        mEmail = email;
        mActivity = activity; //possibility for callback to method in activity class

    }


    //Method to be called from your application.
    //Creates an instance of SetupFeedTask (an AsyncTask) and executes it
    public void setupFeed(String spreadsheet_name, String worksheet_name){

        mSpreadsheetName = spreadsheet_name;
        mWorksheetName = worksheet_name;

        new SetupFeedTask().execute();
    }

    //AsyncTask that handles network comminucation e.t.c.
    private class SetupFeedTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "SetupFeedTask";

        //Executes in its own "worker thread" and doesnt block the main UI thread
        @Override
        protected String doInBackground(Void... params) {

            // Do work
            mToken = fetchToken();

            mSpreadSheetService = new SpreadsheetService("MySpreadsheetService");
            mSpreadSheetService.setAuthSubToken(mToken);

            URL feed_url;
            try {
                feed_url = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
                mFeed = mSpreadSheetService.getFeed(feed_url, SpreadsheetFeed.class);
            }catch(MalformedURLException e){
                //TODO: handle exception
                Log.v(TAG, "MalformedURLException");
                return null;
            }catch(ServiceException e){
                //TODO: handle exception
                Log.v(TAG, "ServiceException");
                return null;
            }catch(IOException e){
                //TODO: handle exception
                Log.v(TAG, "IOException");
                return null;
            }




            try{

                List<SpreadsheetEntry> spreadsheets = mFeed.getEntries();

                // Iterate through all of the spreadsheets returned
                for (SpreadsheetEntry spreadsheet : spreadsheets) {

                    // Print the title of this spreadsheet to the screen
                    Log.v(TAG, "Found Spreadsheet:" + spreadsheet.getTitle().getPlainText());

                    if (spreadsheet.getTitle().getPlainText().equals(mSpreadsheetName)) {

                        List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();

                        //Iterate through worksheets
                        for (WorksheetEntry worksheet : worksheets) {

                            // Print the title of this spreadsheet to the screen
                            Log.v(TAG, "Found Worksheet" + worksheet.getTitle().getPlainText());


                            if (worksheet.getTitle().getPlainText().equals(mWorksheetName)) {

                                mSpreadsheet = spreadsheet;
                                mWorksheet = worksheet;

                                Log.v(TAG, "Spreadsheet and Worksheet is now setup.");

                            }
                        }
                    }
                }

            }catch(ServiceException e){
                //TODO: handle exception
                Log.v(TAG, "Service Exception");
                return null;
            }catch(IOException e){
                //TODO: handle exception
                Log.v(TAG, "IO Exception");
                return null;
            }

            //Just for the example.. mToken not important to return
            return mToken;

        }

        //Call back that is called when doInBackground has finished.
        //Executes in main UI thread
        @Override
        protected void onPostExecute(String result) {
            Log.v("GetTokenTask", "Received token: " + result);

            //TODO: Notify rest of application, e.g.:
            // * Send broadcast
            // * Send message to a handler
            // * Call method on Activity
        }

        //Helper method
        private String fetchToken(){
            try {
                return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
            } catch (UserRecoverableAuthException userRecoverableException) {
                // GooglePlayServices.apk is either old, disabled, or not present, which is
                // recoverable, so we need to show the user some UI through the activity.

                //TODO:
                mActivity.handleException(userRecoverableException);
                if(D) Log.e(TAG, "UserRecoverableAuthException");
            } catch (GoogleAuthException fatalException) {

                //TODO:
                //onError("Unrecoverable error " + fatalException.getMessage(), fatalException);
                if(D) Log.e(TAG, "GoogleAuthException");
            } catch (IOException ioException){

                if(D) Log.e(TAG, "IOException");
            }

            return null;
        }

    }
}
